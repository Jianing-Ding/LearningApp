package com.company.dao.impl;

import com.company.dao.QusetionDao;
import com.company.domain.Question;
import com.company.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuseDaoImpl implements QusetionDao {
    Connection connection=null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    @Override
    public List<Question> getQuestions(int base,int mode,int num, int start) {
        List<Question> list=null;
        try {
            //通过工具类获得连接
            connection = JDBCUtils.getConnetion();
            //通过连接对象获取操作数据库的对象
            String baseStr="";
            if(base==1){
                baseStr="datastructurebase";
            }else if (base==2){
                baseStr="cppdatabase";
            }else if (base==3){
                baseStr="operatesystembase";
            }else{
                baseStr="threadbase";
            }

            String sql="SELECT * FROM "+baseStr+" where mode="+mode+";";//查询sql语句
            ps=connection.prepareStatement(sql);
            //返回查询结果集
            rs=ps.executeQuery();
            //遍历rs，并封装数据
            list=new ArrayList<Question>();
            while(rs.next()) {
                Question question=new Question();
                ArrayList<String> arrayList=new ArrayList<>();
                question.setQuestion(rs.getString(1));//索引从1开始，id参数不用取
                for (int i = 2; i < 8; i++) {
                    arrayList.add(rs.getString(i));
                }
                question.setChoices(arrayList);
                question.setAnswer(rs.getString(8));
                list.add(question);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            JDBCUtils.close(connection, ps, rs);//关闭连接
        }
        if(list!=null)
            list=list.subList(start,start+num);
        return list;
    }

}
