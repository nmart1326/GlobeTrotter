package com.example.globetrotter.model;

/**
 * Represents a single question record from the quiz_questions table.
 */
public class QuizQuestion {
    private final int questionID;
    private final int quizID;
    private final String questionText;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final String correctOption;

    public QuizQuestion(int questionID, int quizID, String questionText,
                        String optionA, String optionB, String optionC, String optionD, String correctOption) {
        this.questionID = questionID;
        this.quizID = quizID;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    // Getters
    public int getQuestionID() {
        return questionID;
    }

    public int getQuizID() {
        return quizID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "questionID=" + questionID +
                ", quizID=" + quizID +
                ", questionText='" + questionText + '\'' +
                ", correctOption='" + correctOption + '\'' +
                '}';
    }
}
