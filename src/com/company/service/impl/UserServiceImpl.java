package com.company.service.impl;

import com.company.dao.UserDao;
import com.company.dao.impl.UserDaoImpl;
import com.company.domain.User;
import com.company.service.UserService;

public class UserServiceImpl implements UserService {

    UserDao dao=new UserDaoImpl();

    /*
     * 主要的逻辑实现
     */
    @Override
    public String checkLogin(User user) {
            User user2=dao.find(user.getUsername());
            if(user2!=null) {
                if (user2.getUsername().equals(user.getUsername()) && user2.getPassword().equals(user.getPassword())) {
                    return "登录成功"+","+user2.getAvatar()+","+user2.getCredit();
                }
                return "登录失败,密码输入错误";
            }else{
                return  "该用户名不存在";
            }
    }

    @Override
    public String register(User user) {
        User user1= dao.find(user.getUsername());
            if(user1!=null) {
                return "注册失败，该用户名已存在！";
            }
        dao.insertElement(user);
        return "注册成功";
    }

    @Override
    public String modifyName(String oldName,String newName) {
        User user=dao.find(newName);
        if (user!=null){
            return "该用户名已存在";
        }
        return dao.updateName(oldName, newName);
    }

    @Override
    public String modifyPassword(String userName,String newPassword) {
        return dao.updatePassword(userName, newPassword);
    }

    @Override
    public String modifyAvatar(String path,String userName) {
         return dao.updateAvatar(path, userName);
    }

    @Override
    public String modifyCredit(int credit, String userName) {
        return dao.updateCredit(credit,userName);
    }

}

