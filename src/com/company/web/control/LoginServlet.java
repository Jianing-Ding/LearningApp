package com.company.web.control;
import com.company.domain.User;
import com.company.service.UserService;
import com.company.service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");//解决请求乱码(post)
        response.setCharacterEncoding("UTF-8");//解决响应乱码,下面要以字符流输出（若字节流输出则要再次编码）

        String sign=request.getParameter("sign");
        PrintWriter out=response.getWriter();
        //把传来的数据封装进javabean中

        UserService service=new UserServiceImpl();
        if (sign.equals("1")||sign.equals("2")) {
            String username=request.getParameter("username");
            String password=request.getParameter("password");
            User user=new User();
            user.setUsername(username);
            user.setPassword(password);
            if ("1".equals(sign)) {//登录操作(设置了一个标记)
                String loginInfo = service.checkLogin(user);
                out.print(loginInfo);
            } else {//注册操作
                String registerInfo = service.register(user);
                out.print(registerInfo);
            }
            System.out.println(username);//在控制台输出
            System.out.println(password);
        }
        if (sign.equals("3")){
            String newName=request.getParameter("username");
            String oldName=request.getParameter("oldName");
            String result=service.modifyName(oldName,newName);
            out.println(result);
        }
        if (sign.equals("4")){
            String newPassword=request.getParameter("password");
            String userName=request.getParameter("username");
            String result=service.modifyPassword(userName,newPassword);
            out.println(result);
        }
        if (sign.equals("5")){
            String avatarPath=request.getParameter("avatarPath");
            String userName=request.getParameter("username");
            String result=service.modifyAvatar(avatarPath,userName);
            out.println(result);
        }
        if(sign.equals("6")){
            String userName=request.getParameter("username");
            int credit=Integer.parseInt(request.getParameter("credit"));
            String result=service.modifyCredit(credit,userName);
            out.println(result);
        }
        System.out.println(sign);


    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}


