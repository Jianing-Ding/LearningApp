package com.company.web.control;

import com.alibaba.fastjson.JSONArray;
import com.company.domain.Goods;
import com.company.service.getGoodsService;
import com.company.service.impl.getGoodsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



    @WebServlet("/getGoodsServlet")
    public class getGoodsServlet  extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            System.out.print("服务器返回数据：");
            request.setCharacterEncoding("UTF-8");//解决请求乱码(post)
            response.setCharacterEncoding("UTF-8");//解决响应乱码,下面要以字符流输出（若字节流输出则要再次编码）
            try {
                PrintWriter out = response.getWriter();
                //把传来的数据封装进javabean中
                getGoodsService service=new getGoodsServiceImpl();
                ArrayList<Goods> list=service.getGoods();
                String arrString ="";
                if (list==null)
                    arrString="数据为空";
                else
                    arrString = JSONArray.toJSONString(list);
                out.write(arrString);
                System.out.print("服务器返回数据：" + arrString);
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                System.out.print("空指针错误： " + e);
            }

        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            doGet(req, resp);
        }

}
