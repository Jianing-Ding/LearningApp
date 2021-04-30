package com.company.dao;

import com.company.domain.Question;

import java.util.List;

public interface QusetionDao {
    public List<Question> getQuestions(int base,int mode,int num,int start);
}
