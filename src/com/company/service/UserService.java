package com.company.service;

import com.company.domain.User;

public interface UserService {
    //查验登录
    public String checkLogin(User user);
    //注册用户
    public String register(User user);
    //修改用户名
    public String modifyName(String oldName,String newName);
    //修改密码
    public String modifyPassword(String userName,String newPassword);
    //修改头像
    public String modifyAvatar(String path,String userName);

    public String modifyCredit(int credit,String userName);
}

