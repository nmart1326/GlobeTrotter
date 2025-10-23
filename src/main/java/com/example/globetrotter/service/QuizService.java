package com.example.globetrotter.service;

import com.example.globetrotter.model.Quiz;
import com.example.globetrotter.model.SqliteQuizDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizService {
    private final SqliteQuizDAO quizDAO;
    private List<String> currentQuestions;
    private int currentIndex;
    private int score;

    public QuizService() {
        quizDAO = new SqliteQuizDAO();
        loadRandomQuestions();
    }

    private void loadRandomQuestions() {
        Quiz quiz = quizDAO.getQuiz(1); // For now, always load the Australian quiz
        if (quiz == null) {
            currentQuestions = new ArrayList<>();
            return;
        }

        currentQuestions = new ArrayList<>();
        Collections.addAll(currentQuestions,
                quiz.getQ1(), quiz.getQ2(), quiz.getQ3(), quiz.getQ4(), quiz.getQ5(),
                quiz.getQ6(), quiz.getQ7(), quiz.getQ8(), quiz.getQ9(), quiz.getQ10());
        Collections.shuffle(currentQuestions);
        currentQuestions = currentQuestions.subList(0, Math.min(5, currentQuestions.size()));
        currentIndex = 0;
        score = 0;
    }

    public String getCurrentQuestion() {
        if (currentIndex < currentQuestions.size()) {
            return currentQuestions.get(currentIndex);
        }
        return null;
    }

    public boolean checkAnswer(String selected, String correctAnswer) {
        boolean correct = selected.equalsIgnoreCase(correctAnswer);
        if (correct) score++;
        return correct;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentIndex() {
        return currentIndex + 1;
    }

    public int getTotalQuestions() {
        return currentQuestions.size();
    }

    public boolean nextQuestion() {
        currentIndex++;
        return currentIndex < currentQuestions.size();
    }
}
