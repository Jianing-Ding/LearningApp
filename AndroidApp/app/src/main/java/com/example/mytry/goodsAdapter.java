package com.example.mytry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static android.content.ContentValues.TAG;

public class goodsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private ArrayList<HashMap<String, Object>> data = new ArrayList<>();

    private HashMap<Integer,Bitmap> map=new HashMap<>();
    private ArrayList<Integer> List;
    /*构造函数*/
    public goodsAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        this.mInflater = LayoutInflater.from(context);
        this.data.addAll(list);
    }

    @Override
    public int getCount() {
        Log.i("info", " answerAdapter getCount: run/ data size:" + data.size());
        return data.size();
    }

    public void update(ArrayList<HashMap<String, Object>> newList) {
        this.data.clear();
        this.data.addAll(newList);
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.goods_item, null);
            holder = new ViewHolder();
            holder.credit = convertView.findViewById(R.id.credit);
            holder.description = convertView.findViewById(R.id.description);
            holder.goodsPic = convertView.findViewById(R.id.picture);
            holder.name = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (data.size() != 0) {
            holder.credit.setText("当前积分：" + data.get(position).get("price").toString());
            holder.name.setText(data.get(position).get("name").toString());
            holder.description.setText(data.get(position).get("description").toString());
            if (data.get(position).get("imagePath")!=null&&!data.get(position).get("imagePath").toString().contains("http")) {
                holder.goodsPic.setImageBitmap((Bitmap)(data.get(position).get("imagePath")));
            } else if (data.get(position).get("imagePath")!=null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Bitmap bmp = getURLimage(data.get(position).get("imagePath").toString().trim());
                        Message msg = new Message();
                        msg.what = 0;
                        map.put(position,bmp);
                        msg.obj = position;
                        handle.sendMessage(msg);
                        System.out.println("000");
                        //handle.sendMessage(msg);
                    }
                }).start();
            }
        }
        return convertView;
    }

    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    data.get(Integer.parseInt(msg.obj.toString())).put("imagePath",map.get(Integer.parseInt(msg.obj.toString())));
                    notifyDataSetChanged();
                    break;
            }
        };
    };


    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            Log.i(TAG, "getURLimage: url"+url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(5000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(true);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }


    public class ViewHolder {
        TextView description;
        ImageView goodsPic;
        TextView credit;
        TextView name;
    }
}