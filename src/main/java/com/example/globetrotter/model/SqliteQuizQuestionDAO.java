package com.example.globetrotter.model;

import com.example.globetrotter.util.SqliteConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for quiz_questions table.
 * Creates table and inserts the 10 Australian quiz questions with answer options.
 */
public class SqliteQuizQuestionDAO {
    private final Connection connection;

    public SqliteQuizQuestionDAO() {
        connection = SqliteConnection.getInstance();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS quiz_questions (
                    QuestionID INTEGER PRIMARY KEY AUTOINCREMENT,
                    QuizID INTEGER NOT NULL,
                    QuestionText VARCHAR NOT NULL,
                    OptionA VARCHAR NOT NULL,
                    OptionB VARCHAR NOT NULL,
                    OptionC VARCHAR NOT NULL,
                    OptionD VARCHAR NOT NULL,
                    CorrectOption VARCHAR NOT NULL,
                    FOREIGN KEY (QuizID) REFERENCES quiz(QuizID)
                );
            """);

            // Check if we already have data
            try (Statement check = connection.createStatement()) {
                ResultSet rs = check.executeQuery("SELECT COUNT(*) AS c FROM quiz_questions");
                if (rs.next() && rs.getInt("c") == 0) {
                    System.out.println("Inserting 10 Australian quiz questions...");
                    insertSampleQuestions();
                } else {
                    System.out.println("Quiz questions already exist, skipping insertion.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring quiz_questions table: " + e.getMessage());
        }
    }

    private void insertSampleQuestions() {
        String sql = """
            INSERT INTO quiz_questions (QuizID, QuestionText, OptionA, OptionB, OptionC, OptionD, CorrectOption)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            // 1
            ps.setInt(1, 1);
            ps.setString(2, "What is the capital city of Australia?");
            ps.setString(3, "Sydney");
            ps.setString(4, "Canberra");
            ps.setString(5, "Melbourne");
            ps.setString(6, "Brisbane");
            ps.setString(7, "B");
            ps.executeUpdate();

            // 2
            ps.setInt(1, 1);
            ps.setString(2, "Which one of these animals is unique to Australia?");
            ps.setString(3, "Kangaroo");
            ps.setString(4, "Elephant");
            ps.setString(5, "Lion");
            ps.setString(6, "Giraffe");
            ps.setString(7, "A");
            ps.executeUpdate();

            // 3
            ps.setInt(1, 1);
            ps.setString(2, "How many states does Australia have?");
            ps.setString(3, "5");
            ps.setString(4, "6");
            ps.setString(5, "7");
            ps.setString(6, "8");
            ps.setString(7, "B");
            ps.executeUpdate();

            // 4
            ps.setInt(1, 1);
            ps.setString(2, "What is the population of Australia (to the nearest million)?");
            ps.setString(3, "15");
            ps.setString(4, "25");
            ps.setString(5, "35");
            ps.setString(6, "45");
            ps.setString(7, "B");
            ps.executeUpdate();

            // 5
            ps.setInt(1, 1);
            ps.setString(2, "Which of these is not the name of an Australian state?");
            ps.setString(3, "Queensland");
            ps.setString(4, "Victoria");
            ps.setString(5, "Tasmania");
            ps.setString(6, "Auckland");
            ps.setString(7, "D");
            ps.executeUpdate();

            // 6
            ps.setInt(1, 1);
            ps.setString(2, "What initially attracted immigrants to Australia?");
            ps.setString(3, "Farming");
            ps.setString(4, "Gold Rush");
            ps.setString(5, "Timber Work");
            ps.setString(6, "Fishing");
            ps.setString(7, "B");
            ps.executeUpdate();

            // 7
            ps.setInt(1, 1);
            ps.setString(2, "What resource does Australia provide the most of to other countries?");
            ps.setString(3, "Coal and Iron Ore");
            ps.setString(4, "Wheat");
            ps.setString(5, "Wool");
            ps.setString(6, "Gold");
            ps.setString(7, "A");
            ps.executeUpdate();

            // 8
            ps.setInt(1, 1);
            ps.setString(2, "What does ANZAC stand for?");
            ps.setString(3, "Australian and New Zealand Army Corps");
            ps.setString(4, "Australian National Zoological Association Council");
            ps.setString(5, "Australian Navy Zone Allied Command");
            ps.setString(6, "Association of New Zealand and Australian Citizens");
            ps.setString(7, "A");
            ps.executeUpdate();

            // 9
            ps.setInt(1, 1);
            ps.setString(2, "What two animals can be found on the Australian crest?");
            ps.setString(3, "Kangaroo and Emu");
            ps.setString(4, "Koala and Platypus");
            ps.setString(5, "Crocodile and Wombat");
            ps.setString(6, "Eagle and Wallaby");
            ps.setString(7, "A");
            ps.executeUpdate();

            // 10
            ps.setInt(1, 1);
            ps.setString(2, "What is the name of the Australian national anthem?");
            ps.setString(3, "Advance Australia Fair");
            ps.setString(4, "God Save the King");
            ps.setString(5, "Waltzing Matilda");
            ps.setString(6, "Southern Cross");
            ps.setString(7, "A");
            ps.executeUpdate();

            System.out.println("✅ All 10 Australian quiz questions inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Error inserting sample questions: " + e.getMessage());
        }
    }

    public List<QuizQuestion> getQuestionsForQuiz(int quizId) {
        List<QuizQuestion> list = new ArrayList<>();
        String sql = "SELECT * FROM quiz_questions WHERE QuizID = ? ORDER BY QuestionID";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new QuizQuestion(
                        rs.getInt("QuestionID"),
                        rs.getInt("QuizID"),
                        rs.getString("QuestionText"),
                        rs.getString("OptionA"),
                        rs.getString("OptionB"),
                        rs.getString("OptionC"),
                        rs.getString("OptionD"),
                        rs.getString("CorrectOption")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading quiz questions: " + e.getMessage());
        }
        return list;
    }

    // Inserts a single GPT-generated question into the quiz_questions table.
    public void addGeneratedQuestion(int quizId, QuizQuestion question) {
        String sql = """
            INSERT INTO quiz_questions
            (QuizID, QuestionText, OptionA, OptionB, OptionC, OptionD, CorrectOption)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            ps.setString(2, question.getQuestionText());
            ps.setString(3, question.getOptionA());
            ps.setString(4, question.getOptionB());
            ps.setString(5, question.getOptionC());
            ps.setString(6, question.getOptionD());
            ps.setString(7, question.getCorrectOption());
            ps.executeUpdate();

            System.out.println("[SqliteQuizQuestionDAO] Added generated question: " + question.getQuestionText());
        } catch (SQLException e) {
            System.err.println("[SqliteQuizQuestionDAO] Error inserting generated question: " + e.getMessage());
        }
    }

}
