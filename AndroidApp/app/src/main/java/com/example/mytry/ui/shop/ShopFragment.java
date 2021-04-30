package com.example.mytry.ui.shop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.example.mytry.Login;
import com.example.mytry.domain.Goods;
import com.example.mytry.domain.Question;
import com.example.mytry.R;
import com.example.mytry.Utils.HttpConnectionUtils;
import com.example.mytry.Utils.StreamChangeStrUtils;
import com.example.mytry.goodsAdapter;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mytry.ui.personal.personalFragment.centerSquareScaleBitmap;

public class ShopFragment extends Fragment {

    private ShopViewModel mViewModel;

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    TextView currentCredit;
    TextView userName;
    ImageView avatar;
    ListView listView;


@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shop_fragment, container, false);

        final SharedPreferences sp=getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);

        userName=layout.findViewById(R.id.userName);
        userName.setText(sp.getString("userName","用户名"));
        currentCredit=layout.findViewById(R.id.credit);
        currentCredit.setText("当前积分："+sp.getString("credit",""));
        avatar=layout.findViewById(R.id.avatar);
        String avatarPath=sp.getString("avatarPath",null);
        if (avatarPath!=null&&!avatarPath.equals("null")){
            showImage(avatarPath);
        }
        ArrayList<HashMap<String,Object>> list=getData();
        final goodsAdapter adapter=new goodsAdapter(getContext(),list);
        listView=layout.findViewById(R.id.goods_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final int price=Integer.parseInt(adapter.getItem(position).get("price").toString());
                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setIcon(R.drawable.ic_notifications_black_24dp);
                builder.setTitle("提示");
                builder.setMessage("您确定要购买该商品吗?");
                // 确定按钮的点击事件

                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int credit=Integer.parseInt(sp.getString("credit",""));
                                if (price<=credit){
                                    credit=credit-price;
                                    SharedPreferences.Editor editor=sp.edit();
                                    editor.putString("credit",""+credit);
                                    editor.apply();
                                    updateCredit(""+credit,sp.getString("userName",""));
                                    Toast.makeText(getContext(),"购买成功",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(),"购买失败，积分不足",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
                // 取消按钮的点击事件
                builder.setNegativeButton("取消",

                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCancelable(true);
                            }

                        });

                builder.create().show();
            }
        });
        return layout;
    }

    public static void updateCredit(final String credit, final String userName) {
        final String[] string = {null};
        Thread thread = new Thread() {

            private HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    Log.i("info", "run: 更新credit线程开始");
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data2 = "credit=" + URLEncoder.encode(credit, "utf-8") + "&userName=" + URLEncoder.encode(userName, "utf-8")+"&sign="+URLEncoder.encode("6","utf-8");
                    connection = HttpConnectionUtils.getConnection(data2);
                    int code = connection.getResponseCode();
                    Log.i("info", "run: 获取response code：" + code);
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        string[0] = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串
                        Log.i("info", string[0]);
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                }
            }
        };//不要忘记开线程
        thread.start();
    }

    /*添加一个得到数据的方法，方便使用*/
    private ArrayList<HashMap<String, Object>> getData() {

        /*为动态数组添加数据*/

        final ArrayList<HashMap<String,Object>> listItem=new ArrayList<>();
        Thread thread=new Thread() {

            private HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    Log.i("info", "run: 获取商品线程开始");
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    connection = HttpConnectionUtils.getConnectionGoods();
                    int code = connection.getResponseCode();
                    Log.i("info", "run: 获取response code："+code);
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String str = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串
                        Log.i("info",str);
                        if (!str.equals("数据为空")) {
                            JSONArray jsonArray = JSONArray.parseArray(str);
                            ArrayList<Goods> goods = JSONArray.parseObject(jsonArray.toJSONString(), new TypeReference<ArrayList<Goods>>() {
                            });
                            for (int i = 0; i < goods.size(); i++) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("name", goods.get(i).getName());
                                map.put("description", goods.get(i).getDescription());
                                map.put("imagePath", goods.get(i).getImagePath());
                                map.put("price", goods.get(i).getPrice());
                                listItem.add(map);
                            }
                        }else{
                            //ToDo 数据为空处理
                        }
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                }
            }
        };//不要忘记开线程
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listItem;
    }

    //加载图片
    public void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        int edgeLength=0;
        if (bm.getWidth()>bm.getHeight())
            edgeLength=bm.getHeight();
        else
            edgeLength=bm.getWidth();
        Bitmap bitmap=centerSquareScaleBitmap(bm,edgeLength);
        avatar.setImageBitmap(bitmap);
    }

}
