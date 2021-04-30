package com.company.service.impl;

import com.company.dao.GoodsDao;
import com.company.dao.impl.GoodsDaoImpl;
import com.company.domain.Goods;
import com.company.service.getGoodsService;

import java.util.ArrayList;

public class getGoodsServiceImpl implements getGoodsService {
    @Override
    public ArrayList<Goods> getGoods() {
        GoodsDao goodsDao=new GoodsDaoImpl();
        ArrayList<Goods> list=goodsDao.getAllGoods();
        return list;
    }
}
