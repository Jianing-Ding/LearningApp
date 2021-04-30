package com.company.dao.impl;

import com.company.dao.GoodsDao;
import com.company.domain.Goods;
import com.company.domain.User;
import com.company.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GoodsDaoImpl implements GoodsDao {
    Connection connection=null;
    PreparedStatement ps=null;
    ResultSet rs=null;

    @Override
    public ArrayList<Goods> getAllGoods() {
        ArrayList<Goods> list=null;
        try {
            //通过工具类获得连接
            connection = JDBCUtils.getConnetion();
            //通过连接对象获取操作数据库的对象
            String sql="SELECT * FROM goods;";//查询sql语句
            ps=connection.prepareStatement(sql);
            //返回查询结果集
            rs=ps.executeQuery();
            //遍历rs，并封装数据
            list=new ArrayList<Goods>();
            while(rs.next()) {
                Goods goods=new Goods(rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5));
                list.add(goods);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            JDBCUtils.close(connection, ps, rs);//关闭连接
        }
        return list;
    }
}
