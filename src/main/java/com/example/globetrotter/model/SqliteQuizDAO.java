package com.example.globetrotter.model;

import com.example.globetrotter.util.SqliteConnection;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteQuizDAO implements IQuizDAO {
    private Connection connection;

    public SqliteQuizDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        insertSampleData();
    }

    // Sample data insertion
    private void insertSampleData() {
        try {
            Statement checkStatement = connection.createStatement();
            ResultSet rs = checkStatement.executeQuery("SELECT COUNT(*) as count FROM quiz");
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Quiz sample data already exists, skipping insertion.");
                return;
            }

            String insertQuery = "INSERT INTO quiz (Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);

            // Sample Quiz 1
            pstmt.setString(1, "What is the capital of France?");
            pstmt.setString(2, "Which planet is known as the Red Planet?");
            pstmt.setString(3, "What is the largest ocean on Earth?");
            pstmt.setString(4, "Who painted the Mona Lisa?");
            pstmt.setString(5, "What is the smallest country in the world?");
            pstmt.setString(6, "Which river is the longest in the world?");
            pstmt.setString(7, "What is the capital of Japan?");
            pstmt.setString(8, "Which continent is the Sahara Desert located in?");
            pstmt.setString(9, "What is the tallest mountain in the world?");
            pstmt.setString(10, "Which country is known as the Land of the Rising Sun?");
            pstmt.addBatch();

            // Sample Quiz 2
            pstmt.setString(1, "What is the chemical symbol for gold?");
            pstmt.setString(2, "How many continents are there?");
            pstmt.setString(3, "What is the capital of Australia?");
            pstmt.setString(4, "Which ocean is the smallest?");
            pstmt.setString(5, "What year did World War II end?");
            pstmt.setString(6, "Which is the largest mammal in the world?");
            pstmt.setString(7, "What is the capital of Canada?");
            pstmt.setString(8, "Which country has the most population?");
            pstmt.setString(9, "What is the longest river in South America?");
            pstmt.setString(10, "Which language has the most native speakers?");
            pstmt.addBatch();

            pstmt.executeBatch();
            System.out.println("Quiz sample data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting quiz sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Table creation
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quiz ("
                    + "QuizID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Q1 VARCHAR,"
                    + "Q2 VARCHAR,"
                    + "Q3 VARCHAR,"
                    + "Q4 VARCHAR,"
                    + "Q5 VARCHAR,"
                    + "Q6 VARCHAR,"
                    + "Q7 VARCHAR,"
                    + "Q8 VARCHAR,"
                    + "Q9 VARCHAR,"
                    + "Q10 VARCHAR"
                    + ")";
            statement.execute(query);
            System.out.println("Quiz table created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating quiz table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add
    @Override
    public void addQuiz(Quiz quiz) {
        String query = "INSERT INTO quiz (Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, quiz.getQ1());
            pstmt.setString(2, quiz.getQ2());
            pstmt.setString(3, quiz.getQ3());
            pstmt.setString(4, quiz.getQ4());
            pstmt.setString(5, quiz.getQ5());
            pstmt.setString(6, quiz.getQ6());
            pstmt.setString(7, quiz.getQ7());
            pstmt.setString(8, quiz.getQ8());
            pstmt.setString(9, quiz.getQ9());
            pstmt.setString(10, quiz.getQ10());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    quiz.setQuizID(generatedKeys.getInt(1));
                }
                System.out.println("Quiz added successfully with ID: " + quiz.getQuizID());
            }
        } catch (SQLException e) {
            System.err.println("Error adding quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update
    @Override
    public void updateQuiz(Quiz quiz) {
        String query = "UPDATE quiz SET Q1 = ?, Q2 = ?, Q3 = ?, Q4 = ?, Q5 = ?, Q6 = ?, Q7 = ?, Q8 = ?, Q9 = ?, Q10 = ? WHERE QuizID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, quiz.getQ1());
            pstmt.setString(2, quiz.getQ2());
            pstmt.setString(3, quiz.getQ3());
            pstmt.setString(4, quiz.getQ4());
            pstmt.setString(5, quiz.getQ5());
            pstmt.setString(6, quiz.getQ6());
            pstmt.setString(7, quiz.getQ7());
            pstmt.setString(8, quiz.getQ8());
            pstmt.setString(9, quiz.getQ9());
            pstmt.setString(10, quiz.getQ10());
            pstmt.setInt(11, quiz.getQuizID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Quiz updated successfully.");
            } else {
                System.out.println("No quiz found with ID: " + quiz.getQuizID());
            }
        } catch (SQLException e) {
            System.err.println("Error updating quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete
    @Override
    public void deleteQuiz(Quiz quiz) {
        String query = "DELETE FROM quiz WHERE QuizID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, quiz.getQuizID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Quiz deleted successfully.");
            } else {
                System.out.println("No quiz found with ID: " + quiz.getQuizID());
            }
        } catch (SQLException e) {
            System.err.println("Error deleting quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get (ID)
    @Override
    public Quiz getQuiz(int QuizID) {
        String query = "SELECT * FROM quiz WHERE QuizID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, QuizID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Quiz(
                        rs.getInt("QuizID"),
                        rs.getString("Q1"),
                        rs.getString("Q2"),
                        rs.getString("Q3"),
                        rs.getString("Q4"),
                        rs.getString("Q5"),
                        rs.getString("Q6"),
                        rs.getString("Q7"),
                        rs.getString("Q8"),
                        rs.getString("Q9"),
                        rs.getString("Q10")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting quiz: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // List all quizzes
    @Override
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quiz ORDER BY QuizID";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Quiz quiz = new Quiz(
                        rs.getInt("QuizID"),
                        rs.getString("Q1"),
                        rs.getString("Q2"),
                        rs.getString("Q3"),
                        rs.getString("Q4"),
                        rs.getString("Q5"),
                        rs.getString("Q6"),
                        rs.getString("Q7"),
                        rs.getString("Q8"),
                        rs.getString("Q9"),
                        rs.getString("Q10")
                );
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all quizzes: " + e.getMessage());
            e.printStackTrace();
        }
        return quizzes;
    }
}