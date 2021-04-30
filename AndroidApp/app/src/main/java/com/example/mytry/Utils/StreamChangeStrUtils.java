package com.example.mytry.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class StreamChangeStrUtils {
    public static String toChange(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//数组流，在流的内部有个缓冲区，可以进行转换成字节
        //下面是属于io流的知识，在此不再赘述
        byte b[]=new byte[1024];
        int len=-1;
        while ((len=inputStream.read(b))!=-1){
            bos.write(b,0,len);
        }
        inputStream.close();//关闭流,数组流会自动关闭，关闭是否都可以
        String str = new String(bos.toByteArray());
        //服务器默认返回的是gbk，如果要在android端解决乱码，可以在此设置为gbk,一般提倡的是服务器解决
        // 让服务器给我们返回utf-8，因为在android本地默认的是utf-8
        return str;
    }

}
