package com.company.domain;

import java.util.ArrayList;

public class Question {
    String question;
    String choiceA;
    String choiceB;
    String choiceC;
    String choiceD;
    String choiceE;
    String choiceF;
    String answer;

    public Question() {
        this.question=null;
        this.answer=null;
        this.choiceA=null;
        this.choiceB=null;
        this.choiceC=null;
        this.choiceD=null;
        this.choiceE=null;
        this.choiceF=null;
    }

    public Question(String question,String choiceA,String choiceB,String choiceC,String choiceD,String choiceE,String choiceF,String answer) {
        this.answer=answer;
        this.choiceA=choiceA;
        this.choiceB=choiceB;
        this.choiceC=choiceC;
        this.choiceD=choiceD;
        this.choiceE=choiceE;
        this.choiceF=choiceF;
        this.question=question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getChoiceE() {
        return choiceE;
    }

    public void setChoiceE(String choiceE) {
        this.choiceE = choiceE;
    }

    public String getChoiceF() {
        return choiceF;
    }

    public void setChoiceF(String choiceF) {
        this.choiceF = choiceF;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setChoices(ArrayList<String> choices){
        this.choiceA=choices.get(0);
        this.choiceB=choices.get(1);
        this.choiceC=choices.get(2);
        this.choiceD=choices.get(3);
        this.choiceE=choices.get(4);
        this.choiceF=choices.get(5);
    }
}
