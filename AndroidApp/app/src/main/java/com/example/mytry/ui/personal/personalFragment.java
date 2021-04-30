package com.example.mytry.ui.personal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mytry.AboutUs;
import com.example.mytry.Login;
import com.example.mytry.R;
import com.example.mytry.Utils.HttpConnectionUtils;
import com.example.mytry.Utils.StreamChangeStrUtils;
import com.example.mytry.setPassword;
import com.example.mytry.setUsername;
import com.example.mytry.ui.onLineTest.questionBaseChoose;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class personalFragment extends Fragment {
    private static final int IMAGE = 1;
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private NotificationsViewModel notificationsViewModel;
    private SharedPreferences sp;
    public static TextView userName;
    ImageView avatar;
    CardView avatarChange;
    CardView userNameChange;
    CardView passwordChange;
    CardView aboutUs;
    CardView logout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        userName=root.findViewById(R.id.description);
        avatar=root.findViewById(R.id.avatar);
        avatarChange=root.findViewById(R.id.avatarChange);
        userNameChange=root.findViewById(R.id.usernameChange);
        passwordChange=root.findViewById(R.id.passwordChange);
        aboutUs=root.findViewById(R.id.aboutus);
        logout=root.findViewById(R.id.logout);
        sp=root.getContext().getSharedPreferences("info",Context.MODE_PRIVATE);
        String avatarPath=sp.getString("avatarPath",null);
        if (avatarPath!=null&&!avatarPath.equals("null")){
            showImage(avatarPath);
        }
        userNameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsername setUserName=new setUsername();
                setUserName.setWindow(getActivity());
                questionBaseChoose.setBackgroundAlpha(0.3f,getActivity());
                setUserName.showAtLocation(getView(), Gravity.CENTER,0,0);
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassword setPassword=new setPassword();
                setPassword.setView(getContext());
                questionBaseChoose.setBackgroundAlpha(0.3f,getActivity());
                setPassword.showAtLocation(getView(),Gravity.CENTER,0,0);
            }
        });
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                verifyStoragePermissions(getActivity());
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        };
        avatarChange.setOnClickListener(listener);
        avatar.setOnClickListener(listener);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AboutUs.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setIcon(R.drawable.ic_notifications_black_24dp);
                builder.setTitle("提示");
                builder.setMessage("您确定要退出登录吗?\n点击确定返回登录页面");
                // 确定按钮的点击事件

                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor=sp.edit();
                                editor.putString("status","0");
                                editor.apply();
                                Intent intent=new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                getActivity().finish();
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
        userName.setText(sp.getString("userName","用户名"));
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("avatarPath",imagePath);
            editor.apply();
            Toast.makeText(getContext(),uploadAvatar(imagePath),Toast.LENGTH_SHORT).show();
            c.close();
        }
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

    private String uploadAvatar(final String imagePath){
        final String[] result = {null};
        Thread thread=new Thread() {

            private HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data2 = "avatarPath="+ URLEncoder.encode(imagePath,"utf-8")+"&username="+URLEncoder.encode(sp.getString("userName",""),"utf-8")+"&sign="+URLEncoder.encode("5","utf-8");
                    connection = HttpConnectionUtils.getConnection(data2);
                    int code = connection.getResponseCode();
                    Log.i("info", "run: 获取response code："+code);
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        result[0] = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串
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
        return result[0];
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }

    }

    /**

     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
     * @return  缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
    {
        if(null == bitmap || edgeLength <= 0)
        {
            return  null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg >= edgeLength && heightOrg >= edgeLength)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try{
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            }
            catch(Exception e){
                return null;
            }
        }

        return result;
    }



}