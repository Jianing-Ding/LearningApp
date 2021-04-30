package com.company.service.impl;

import com.company.dao.QusetionDao;
import com.company.dao.impl.QuseDaoImpl;
import com.company.domain.Question;
import com.company.service.getQuestionsService;

import java.util.List;

public class getQuseServiceImpl implements getQuestionsService {
    @Override
    public List<Question> getQusetions(int base,int start, int length, int mode) {
        List<Question> list=null;
        QusetionDao qusetionDao=new QuseDaoImpl();
        list=qusetionDao.getQuestions(base,mode,length,start);
        return list;
    }
}
