package com.example.globetrotter.model;

import java.util.*;

// MOCK!!! Doesn't use the database!!!
public class MockQuizDAO implements IQuizDAO {

    private Map<Integer, Quiz> quizzes;
    private int nextId;

    public MockQuizDAO() {
        quizzes = new HashMap<>();
        nextId = 1;
        insertInitialQuiz();
    }

    // Initial quiz data insertion
    private void insertInitialQuiz() {
        // Check if QuizID 1 already exists
        if (quizzes.containsKey(1)) {
            return;
        }

        // Australian Quiz - always at QuizID 1
        Quiz quiz1 = new Quiz(
                1,
                "What is the capital city of Australia?",
                "Which one of these animals is unique to Australia?",
                "How many states does Australia have?",
                "What is the population of Australia (to the nearest million)?",
                "Which of these is not the name of an Australian state?",
                "What initially attracted immigrants to Australia?",
                "What resource does Australia provide the most of to other countries?",
                "What does ANZAC stand for?",
                "What two animals can be found on the Australian crest?",
                "What is the name of the Australian national anthem?"
        );
        quizzes.put(1, quiz1);
        nextId = 2;
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