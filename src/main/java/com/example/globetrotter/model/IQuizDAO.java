package com.example.globetrotter.model;

import java.util.List;

public interface IQuizDAO {
    public void addQuiz(Quiz quiz);
    public void updateQuiz(Quiz quiz);
    public void deleteQuiz(Quiz quiz);
    public Quiz getQuiz(int QuizID);
    public List<Quiz> getAllQuizzes();
}