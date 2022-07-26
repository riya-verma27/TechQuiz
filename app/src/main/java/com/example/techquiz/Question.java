package com.example.techquiz;

public class Question {
    String Question;
    String optionA;
    String optionB;
    String optionC;
    String optionD;
    int correct_ans;

    public Question(String Question, String optionA, String optionB, String optionC, String optionD, int correct_ans) {
        this.Question = Question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct_ans = correct_ans;
    }
    public String getQuestion()
    {
        return Question;
    }
    public void setQuestion()
    {
        this.Question=Question;
    }

    public String getOptionA()
    {
        return optionA;
    }
    public void setOptionA()
    {
        this.optionA=optionA;
    }
    public String getOptionB()
    {
        return optionB;
    }
    public void setOptionB()
    {
        this.optionB=optionB;
    }
    public String getOptionC()
    {
        return optionC;
    }
    public void setOptionD()
    {
        this.optionD=optionD;
    }
    public String getOptionD()
    {
        return optionD;
    }
    public int getCorrectAns()
    {
        return correct_ans;
    }
    public void setCorrect_ans()
    {
        this.correct_ans=correct_ans;
    }
}
