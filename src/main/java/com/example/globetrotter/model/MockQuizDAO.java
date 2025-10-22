package com.example.globetrotter.model;

import java.util.*;

// MOCK!!! Doesn't use the database!!!
public class MockQuizDAO implements IQuizDAO {

    private Map<Integer, Quiz> quizzes;
    private int nextId;

    public MockQuizDAO() {
        quizzes = new HashMap<>();
        nextId = 1;
        insertSampleData();
    }

    // Sample data insertion
    private void insertSampleData() {
        // Sample Quiz 1
        Quiz quiz1 = new Quiz(
                "What is the capital of France?",
                "Which planet is known as the Red Planet?",
                "What is the largest ocean on Earth?",
                "Who painted the Mona Lisa?",
                "What is the smallest country in the world?",
                "Which river is the longest in the world?",
                "What is the capital of Japan?",
                "Which continent is the Sahara Desert located in?",
                "What is the tallest mountain in the world?",
                "Which country is known as the Land of the Rising Sun?"
        );
        addQuiz(quiz1);

        // Sample Quiz 2
        Quiz quiz2 = new Quiz(
                "What is the chemical symbol for gold?",
                "How many continents are there?",
                "What is the capital of Australia?",
                "Which ocean is the smallest?",
                "What year did World War II end?",
                "Which is the largest mammal in the world?",
                "What is the capital of Canada?",
                "Which country has the most population?",
                "What is the longest river in South America?",
                "Which language has the most native speakers?"
        );
        addQuiz(quiz2);
    }

    // Add
    @Override
    public void addQuiz(Quiz quiz) {
        // Check entry is not null
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz cannot be null");
        }



        // Give quiz an ID
        if (quiz.getQuizID() == 0) {
            quiz.setQuizID(nextId++);
        } else {
            if (quiz.getQuizID() >= nextId) {
                nextId = quiz.getQuizID() + 1;
            }
        }

        quizzes.put(quiz.getQuizID(), quiz);
    }

    // Update
    @Override
    public void updateQuiz(Quiz quiz) {
        // Check entry is not null
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz cannot be null");
        }

        // Check quiz entry exists
        if (!quizzes.containsKey(quiz.getQuizID())) {
            throw new IllegalArgumentException("Quiz with ID " + quiz.getQuizID() + " does not exist");
        }



        quizzes.put(quiz.getQuizID(), quiz);
    }

    // Delete
    @Override
    public void deleteQuiz(Quiz quiz) {
        // Check entry is not null
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz cannot be null");
        }

        // Check quiz entry exists
        if (!quizzes.containsKey(quiz.getQuizID())) {
            throw new IllegalArgumentException("Quiz with ID " + quiz.getQuizID() + " does not exist");
        }

        quizzes.remove(quiz.getQuizID());
    }

    // Get (ID)
    @Override
    public Quiz getQuiz(int quizID) {
        return quizzes.get(quizID);
    }

    // List
    @Override
    public List<Quiz> getAllQuizzes() {
        return new ArrayList<>(quizzes.values());
    }

    // Count
    public int getQuizCount() {
        return quizzes.size();
    }

    public boolean quizExists(int quizID) {
        return quizzes.containsKey(quizID);
    }
}