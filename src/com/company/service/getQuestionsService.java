package com.company.service;

import com.company.domain.Question;

import java.util.List;

public interface getQuestionsService {
    public List<Question> getQusetions(int base,int start,int length,int mode);
}
