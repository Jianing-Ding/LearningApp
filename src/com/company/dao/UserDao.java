package com.company.dao;


import java.util.List;

import com.company.domain.User;

public interface UserDao {
    //找到所有元素,用来验证登录信息
    public  List<User> findAll();
    public User find(String userName);
    //插入元素,用来注册
    public void insertElement(User people);
    //修改用户名
    public String updateName(String oldName,String newName);
    //修改密码
    public String updatePassword(String userName,String password);
    //修改头像
    public String updateAvatar(String imagePath,String userName);

    public String updateCredit(int credit,String username);
}