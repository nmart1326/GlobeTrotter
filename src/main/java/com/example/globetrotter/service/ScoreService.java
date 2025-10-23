package com.example.globetrotter.service;

import com.example.globetrotter.model.Score;
import com.example.globetrotter.model.SqliteScoreDAO;

// Saves and tracks each quiz attempt
public class ScoreService {

    private final SqliteScoreDAO scoreDAO = new SqliteScoreDAO();


    public void saveScore(int userId, int quizId, int score) {
        try {
            Score newScore = new Score(userId, quizId, score);
            scoreDAO.addScore(newScore);
            System.out.println("New score recorded: UserID=" + userId + " QuizID=" + quizId + " Score=" + score);
        } catch (Exception e) {
            System.err.println("Error saving score: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
