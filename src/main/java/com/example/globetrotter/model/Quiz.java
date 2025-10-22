package com.example.globetrotter.model;

public class Quiz {
    private int QuizID;
    private String Q1;
    private String Q2;
    private String Q3;
    private String Q4;
    private String Q5;
    private String Q6;
    private String Q7;
    private String Q8;
    private String Q9;
    private String Q10;

    // Constructor without QuizID
    public Quiz(String Q1, String Q2, String Q3, String Q4, String Q5,
                String Q6, String Q7, String Q8, String Q9, String Q10) {
        this.Q1 = Q1;
        this.Q2 = Q2;
        this.Q3 = Q3;
        this.Q4 = Q4;
        this.Q5 = Q5;
        this.Q6 = Q6;
        this.Q7 = Q7;
        this.Q8 = Q8;
        this.Q9 = Q9;
        this.Q10 = Q10;
    }

    // Constructor with QuizID
    public Quiz(int QuizID, String Q1, String Q2, String Q3, String Q4, String Q5,
                String Q6, String Q7, String Q8, String Q9, String Q10) {
        this.QuizID = QuizID;
        this.Q1 = Q1;
        this.Q2 = Q2;
        this.Q3 = Q3;
        this.Q4 = Q4;
        this.Q5 = Q5;
        this.Q6 = Q6;
        this.Q7 = Q7;
        this.Q8 = Q8;
        this.Q9 = Q9;
        this.Q10 = Q10;
    }

    // Getters and setters
    public int getQuizID() {
        return QuizID;
    }

    public void setQuizID(int QuizID) {
        this.QuizID = QuizID;
    }

    public String getQ1() {
        return Q1;
    }

    public void setQ1(String Q1) {
        this.Q1 = Q1;
    }

    public String getQ2() {
        return Q2;
    }

    public void setQ2(String Q2) {
        this.Q2 = Q2;
    }

    public String getQ3() {
        return Q3;
    }

    public void setQ3(String Q3) {
        this.Q3 = Q3;
    }

    public String getQ4() {
        return Q4;
    }

    public void setQ4(String Q4) {
        this.Q4 = Q4;
    }

    public String getQ5() {
        return Q5;
    }

    public void setQ5(String Q5) {
        this.Q5 = Q5;
    }

    public String getQ6() {
        return Q6;
    }

    public void setQ6(String Q6) {
        this.Q6 = Q6;
    }

    public String getQ7() {
        return Q7;
    }

    public void setQ7(String Q7) {
        this.Q7 = Q7;
    }

    public String getQ8() {
        return Q8;
    }

    public void setQ8(String Q8) {
        this.Q8 = Q8;
    }

    public String getQ9() {
        return Q9;
    }

    public void setQ9(String Q9) {
        this.Q9 = Q9;
    }

    public String getQ10() {
        return Q10;
    }

    public void setQ10(String Q10) {
        this.Q10 = Q10;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "QuizID=" + QuizID +
                ", Q1='" + Q1 + '\'' +
                ", Q2='" + Q2 + '\'' +
                ", Q3='" + Q3 + '\'' +
                ", Q4='" + Q4 + '\'' +
                ", Q5='" + Q5 + '\'' +
                ", Q6='" + Q6 + '\'' +
                ", Q7='" + Q7 + '\'' +
                ", Q8='" + Q8 + '\'' +
                ", Q9='" + Q9 + '\'' +
                ", Q10='" + Q10 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quiz quiz = (Quiz) obj;
        return QuizID == quiz.QuizID;
    }
}