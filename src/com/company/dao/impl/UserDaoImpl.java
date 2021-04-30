package com.company.dao.impl;

import com.company.dao.UserDao;
import com.company.domain.User;
import com.company.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class UserDaoImpl implements UserDao {

    Connection connection=null;
    PreparedStatement ps=null;
    ResultSet rs=null;

    /**
     * dao层，从数据库里面取出数据
     */
    @Override
    public List<User> findAll() {
        List<User> list=null;
        try {
            //通过工具类获得连接
            connection = JDBCUtils.getConnetion();
            //通过连接对象获取操作数据库的对象
            String sql="SELECT * FROM users;";//查询sql语句
            ps=connection.prepareStatement(sql);
            //返回查询结果集
            rs=ps.executeQuery();
            //遍历rs，并封装数据
            list=new ArrayList<User>();
            while(rs.next()) {
                User user=new User();
                user.setUsername(rs.getString(2));//索引从1开始，id参数不用取
                user.setPassword(rs.getString(3));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            JDBCUtils.close(connection, ps, rs);//关闭连接
        }
        return list;
    }

    public User find(String userName) {
        User user=new User();
        try {
            //通过工具类获得连接
            connection = JDBCUtils.getConnetion();
            //通过连接对象获取操作数据库的对象
            String sql="SELECT * FROM users where UserName=?;";//查询sql语句
            ps=connection.prepareStatement(sql);
            ps.setString(1,userName);
            //返回查询结果集
            rs=ps.executeQuery();
            //遍历rs，并封装数据
            if (rs.next()){
                user.setUsername(rs.getString(2));//索引从1开始，id参数不用取
                user.setPassword(rs.getString(3));
                user.setAvatar(rs.getString(4));
                user.setCredit(rs.getInt(5));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            JDBCUtils.close(connection, ps, rs);//关闭连接
        }
        return null;
    }

    @Override
    public void insertElement(User people) {
        try {
            connection= JDBCUtils.getConnetion();
            String sql="INSERT INTO users(username,upassword) VALUES(?,?);";//插入语句
            ps=connection.prepareStatement(sql);
            ps.setString(1,people.getUsername());//使用prepareStatement可以防止sql注入
            ps.setString(2,people.getPassword());
            //执行更新语句
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtils.close(connection, ps, rs);
        }
    }

    @Override
    public String updateName(String oldName, String newName) {
        try {
            connection= JDBCUtils.getConnetion();
            String sql="UPDATE users SET UserName=? where UserName=?;";//插入语句
            ps=connection.prepareStatement(sql);
            ps.setString(1,newName);//使用prepareStatement可以防止sql注入
            ps.setString(2,oldName);
            //执行更新语句
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return "数据库更新失败";
        }
        finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return "修改成功";
    }

    @Override
    public String updatePassword(String userName, String password) {
        try {
            connection= JDBCUtils.getConnetion();
            String sql="UPDATE users set uPassword=? where UserName=?;";//插入语句
            ps=connection.prepareStatement(sql);
            ps.setString(1,password);//使用prepareStatement可以防止sql注入
            ps.setString(2,userName);
            //执行更新语句
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return "数据库更新失败";
        }
        finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return "修改成功";
    }

    @Override
    public String updateAvatar(String imagePath,String userName) {
        try {
            connection= JDBCUtils.getConnetion();
            String sql="UPDATE users set avatarPath=? where UserName=?;";//插入语句
            ps=connection.prepareStatement(sql);
            ps.setString(1,imagePath);
            ps.setString(2,userName);
            //执行更新语句
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return "数据库更新失败";
        }
        finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return "修改成功";
    }

    @Override
    public String updateCredit(int credit,String userName) {
        try {
            connection= JDBCUtils.getConnetion();
            String sql="UPDATE users set credit=? where UserName=?;";//插入语句
            ps=connection.prepareStatement(sql);
            ps.setInt(1,credit);
            ps.setString(2,userName);
            //执行更新语句
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return "数据库更新失败";
        }
        finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return "修改成功";
    }
}


