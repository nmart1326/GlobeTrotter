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

    // Initial quiz data insertion
    private void insertSampleData() {
        try {
            // Check if QuizID 1 already exists
            String checkQuery = "SELECT COUNT(*) as count FROM quiz WHERE QuizID = 1";
            Statement checkStatement = connection.createStatement();
            ResultSet rs = checkStatement.executeQuery(checkQuery);
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Australian quiz already exists, skipping insertion.");
                return;
            }

            String insertQuery = "INSERT INTO quiz (Title, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);

            // Australian Quiz
            pstmt.setString(1, "Australian Knowledge Quiz");
            pstmt.setString(2, "What is the capital city of Australia?");
            pstmt.setString(3, "Which one of these animals is unique to Australia?");
            pstmt.setString(4, "How many states does Australia have?");
            pstmt.setString(5, "What is the population of Australia (to the nearest million)?");
            pstmt.setString(6, "Which of these is not the name of an Australian state?");
            pstmt.setString(7, "What initially attracted immigrants to Australia?");
            pstmt.setString(8, "What resource does Australia provide the most of to other countries?");
            pstmt.setString(9, "What does ANZAC stand for?");
            pstmt.setString(10, "What two animals can be found on the Australian crest?");
            pstmt.setString(11, "What is the name of the Australian national anthem?");

            pstmt.executeUpdate();
            System.out.println("Australian quiz inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting Australian quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Table creation
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quiz ("
                    + "QuizID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Title VARCHAR,"  // add this for quiz_title support
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

            // add this for existing database upgrade
            statement.execute("ALTER TABLE quiz ADD COLUMN Title VARCHAR;");
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate column name")) {
                System.out.println("Column Title already exists — skipping ALTER TABLE.");
            } else {
                System.err.println("Error creating quiz table: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    // Add
    @Override
    public void addQuiz(Quiz quiz) {
        String query = "INSERT INTO quiz (Title, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, quiz.getTitle());
            pstmt.setString(2, quiz.getQ1());
            pstmt.setString(3, quiz.getQ2());
            pstmt.setString(4, quiz.getQ3());
            pstmt.setString(5, quiz.getQ4());
            pstmt.setString(6, quiz.getQ5());
            pstmt.setString(7, quiz.getQ6());
            pstmt.setString(8, quiz.getQ7());
            pstmt.setString(9, quiz.getQ8());
            pstmt.setString(10, quiz.getQ9());
            pstmt.setString(11, quiz.getQ10());

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
        String query = "UPDATE quiz SET Title = ?, Q1 = ?, Q2 = ?, Q3 = ?, Q4 = ?, Q5 = ?, Q6 = ?, Q7 = ?, Q8 = ?, Q9 = ?, Q10 = ? WHERE QuizID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, quiz.getTitle());
            pstmt.setString(2, quiz.getQ1());
            pstmt.setString(3, quiz.getQ2());
            pstmt.setString(4, quiz.getQ3());
            pstmt.setString(5, quiz.getQ4());
            pstmt.setString(6, quiz.getQ5());
            pstmt.setString(7, quiz.getQ6());
            pstmt.setString(8, quiz.getQ7());
            pstmt.setString(9, quiz.getQ8());
            pstmt.setString(10, quiz.getQ9());
            pstmt.setString(11, quiz.getQ10());
            pstmt.setInt(12, quiz.getQuizID());

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
                        rs.getString("Title"),
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
                        rs.getString("Title"),
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