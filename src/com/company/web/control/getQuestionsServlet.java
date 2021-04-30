package com.company.web.control;

import com.alibaba.fastjson.JSONArray;
import com.company.dao.QusetionDao;
import com.company.dao.impl.QuseDaoImpl;
import com.company.domain.Question;
import com.company.service.getQuestionsService;
import com.company.service.impl.getQuseServiceImpl;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/getQuestionsServlet")
public class getQuestionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("服务器返回数据：");
        request.setCharacterEncoding("UTF-8");//解决请求乱码(post)
        response.setCharacterEncoding("UTF-8");//解决响应乱码,下面要以字符流输出（若字节流输出则要再次编码）
        try {
            System.out.print(request.getParameter("base"));
            int start = Integer.parseInt(request.getParameter("start").trim());
            int length = Integer.parseInt(request.getParameter("length").trim());
            int mode = Integer.parseInt(request.getParameter("mode").trim());
            int base = Integer.parseInt(request.getParameter("base").trim());
            PrintWriter out=response.getWriter();
            //把传来的数据封装进javabean中
            getQuestionsService getQuestionsService=new getQuseServiceImpl();
            List<Question> list=getQuestionsService.getQusetions(base,start,length,mode);
            System.out.print(list.get(0).getQuestion());
            String arrString = JSONArray.toJSONString(list);
            out.write(arrString);
            System.out.print("服务器返回数据："+arrString);
        }catch (NullPointerException | ArrayIndexOutOfBoundsException e){
            System.out.print("空指针错误： "+e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("返回数据：");
        doGet(req, resp);
    }
}
