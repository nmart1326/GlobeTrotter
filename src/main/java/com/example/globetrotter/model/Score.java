package com.example.globetrotter.model;

public class Score {
    private int ScoreID;
    private int UserID;
    private int QuizID;
    private int HighScore;

    // Constructor without ScoreID
    public Score(int UserID, int QuizID, int HighScore) {
        this.UserID = UserID;
        this.QuizID = QuizID;
        this.HighScore = HighScore;
    }

    // Constructor with ScoreID
    public Score(int ScoreID, int UserID, int QuizID, int HighScore) {
        this.ScoreID = ScoreID;
        this.UserID = UserID;
        this.QuizID = QuizID;
        this.HighScore = HighScore;
    }

    // Getters and setters
    public int getScoreID() {
        return ScoreID;
    }

    public void setScoreID(int ScoreID) {
        this.ScoreID = ScoreID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public int getQuizID() {
        return QuizID;
    }

    public void setQuizID(int QuizID) {
        this.QuizID = QuizID;
    }

    public int getHighScore() {
        return HighScore;
    }

    public void setHighScore(int HighScore) {
        this.HighScore = HighScore;
    }

    @Override
    public String toString() {
        return "Score{" +
                "UserID=" + UserID +
                ", QuizID=" + QuizID +
                ", HighScore=" + HighScore +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Score score = (Score) obj;
        return ScoreID == score.ScoreID;
    }
}