package com.example.globetrotter.model;

import java.util.List;

public interface IScoreDAO {
    public void addScore(Score score);
    public void updateScore(Score score);
    public void deleteScore(Score score);
    public Score getScore(int ScoreID);
    public List<Score> getAllScores();
    public Score getScoreByUserAndQuiz(int UserID, int QuizID);
    public List<Score> getScoresByUser(int UserID);
    public List<Score> getScoresByQuiz(int QuizID);
}