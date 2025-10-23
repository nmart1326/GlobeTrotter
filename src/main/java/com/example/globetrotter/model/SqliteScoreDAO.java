package com.example.globetrotter.model;

import com.example.globetrotter.util.SqliteConnection;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteScoreDAO implements IScoreDAO {
    private Connection connection;

    public SqliteScoreDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
       //Not needed anymore (was just for testing) insertSampleData();
    }

    // Sample data insertion
    private void insertSampleData() {
        try {
            Statement checkStatement = connection.createStatement();
            ResultSet rs = checkStatement.executeQuery("SELECT COUNT(*) as count FROM score");
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Score sample data already exists, skipping insertion.");
                return;
            }

            String insertQuery = "INSERT INTO score (UserID, QuizID, HighScore) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);

            // Assuming UserID 1 and QuizID 1
            pstmt.setInt(1, 1);
            pstmt.setInt(2, 1);
            pstmt.setInt(3, 85);
            pstmt.addBatch();

            // Assuming UserID 3 and QuizID 1
            pstmt.setInt(1, 3);
            pstmt.setInt(2, 1);
            pstmt.setInt(3, 92);
            pstmt.addBatch();

            // Assuming UserID 1 and QuizID 2
            pstmt.setInt(1, 1);
            pstmt.setInt(2, 2);
            pstmt.setInt(3, 45);
            pstmt.addBatch();

            pstmt.executeBatch();
            System.out.println("Score sample data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting score sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Table creation
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS score ("
                    + "ScoreID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "UserID INTEGER NOT NULL,"
                    + "QuizID INTEGER NOT NULL,"
                    + "HighScore INTEGER NOT NULL DEFAULT 0,"
                    + "FOREIGN KEY (UserID) REFERENCES users(UserID) ON DELETE CASCADE,"
                    + "FOREIGN KEY (QuizID) REFERENCES quiz(QuizID) ON DELETE CASCADE"
                    + ")";
            statement.execute(query);

            // Create indexes
            statement.execute("CREATE INDEX IF NOT EXISTS idx_score_userid ON score(UserID)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_score_quizid ON score(QuizID)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_score_highscore ON score(HighScore)");

            System.out.println("Score table created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating score table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add
    @Override
    public void addScore(Score score) {
        String query = "INSERT INTO score (UserID, QuizID, HighScore) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, score.getUserID());
            pstmt.setInt(2, score.getQuizID());
            pstmt.setInt(3, score.getHighScore());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    score.setScoreID(generatedKeys.getInt(1));
                }
                System.out.println("Score added successfully with ID: " + score.getScoreID());
            }
        } catch (SQLException e) {
            System.err.println("Error adding score: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update
    @Override
    public void updateScore(Score score) {
        String query = "UPDATE score SET UserID = ?, QuizID = ?, HighScore = ? WHERE ScoreID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, score.getUserID());
            pstmt.setInt(2, score.getQuizID());
            pstmt.setInt(3, score.getHighScore());
            pstmt.setInt(4, score.getScoreID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Score updated successfully.");
            } else {
                System.out.println("No score found with ID: " + score.getScoreID());
            }
        } catch (SQLException e) {
            System.err.println("Error updating score: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete
    @Override
    public void deleteScore(Score score) {
        String query = "DELETE FROM score WHERE ScoreID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, score.getScoreID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Score deleted successfully.");
            } else {
                System.out.println("No score found with ID: " + score.getScoreID());
            }
        } catch (SQLException e) {
            System.err.println("Error deleting score: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get (ID)
    @Override
    public Score getScore(int ScoreID) {
        String query = "SELECT * FROM score WHERE ScoreID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, ScoreID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Score(
                        rs.getInt("ScoreID"),
                        rs.getInt("UserID"),
                        rs.getInt("QuizID"),
                        rs.getInt("HighScore")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting score: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get score by UserID and QuizID
    @Override
    public Score getScoreByUserAndQuiz(int UserID, int QuizID) {
        String query = "SELECT * FROM score WHERE UserID = ? AND QuizID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, UserID);
            pstmt.setInt(2, QuizID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Score(
                        rs.getInt("ScoreID"),
                        rs.getInt("UserID"),
                        rs.getInt("QuizID"),
                        rs.getInt("HighScore")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting score by user and quiz: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get all scores for a user
    @Override
    public List<Score> getScoresByUser(int UserID) {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM score WHERE UserID = ? ORDER BY HighScore DESC";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, UserID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Score score = new Score(
                        rs.getInt("ScoreID"),
                        rs.getInt("UserID"),
                        rs.getInt("QuizID"),
                        rs.getInt("HighScore")
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            System.err.println("Error getting scores by user: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }

    // Get all scores for a quiz
    @Override
    public List<Score> getScoresByQuiz(int QuizID) {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM score WHERE QuizID = ? ORDER BY HighScore DESC";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, QuizID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Score score = new Score(
                        rs.getInt("ScoreID"),
                        rs.getInt("UserID"),
                        rs.getInt("QuizID"),
                        rs.getInt("HighScore")
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            System.err.println("Error getting scores by quiz: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }

    // List all scores
    @Override
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM score ORDER BY ScoreID";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Score score = new Score(
                        rs.getInt("ScoreID"),
                        rs.getInt("UserID"),
                        rs.getInt("QuizID"),
                        rs.getInt("HighScore")
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all scores: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }
}