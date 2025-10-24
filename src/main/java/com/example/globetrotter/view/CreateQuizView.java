package com.example.globetrotter.view;

import com.example.globetrotter.model.*;
import com.example.globetrotter.service.QuizGeneratorService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * View allowing users to generate a new quiz using AI and save it to the database.
 */
public class CreateQuizView extends StackPane {

    private final QuizGeneratorService generator = new QuizGeneratorService();
    private final SqliteQuizDAO quizDAO = new SqliteQuizDAO();
    private final SqliteQuizQuestionDAO questionDAO = new SqliteQuizQuestionDAO();
    private final StackPane root;

    public CreateQuizView(StackPane root) {
        this.root = root;

        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgba(255,255,255,0.01);");

        // === Main white card ===
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: #E5E7EB;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0.25, 0, 6);
            -fx-padding: 40;
        """);

        Label title = new Label("Create a New Quiz");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        TextField topicInput = new TextField();
        topicInput.setPromptText("Enter a quiz topic (e.g. Australian Wildlife)");
        topicInput.setMaxWidth(320);
        topicInput.setStyle("""
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-padding: 10;
            -fx-font-size: 14px;
        """);

        Button generateBtn = new Button("Generate Quiz with AI");
        generateBtn.setStyle("""
            -fx-background-color: #2563EB;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 12 24;
            -fx-max-width: 260px;
        """);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #374151; -fx-font-size: 14px;");

        Button backBtn = new Button("Return to Selection");
        backBtn.setStyle("""
            -fx-background-color: #6B7280;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-padding: 8 20;
        """);
        backBtn.setOnAction(e -> {
            root.getChildren().remove(this);
            QuizSelectionView selectionView = new QuizSelectionView(root);
            root.getChildren().add(selectionView);
            selectionView.toFront();
        });

        // === Close button overlay ===
        Image closeImage = new Image(getClass().getResourceAsStream("/assets/icons/close.png"));
        ImageView closeImageView = new ImageView(closeImage);
        closeImageView.setFitWidth(24);
        closeImageView.setFitHeight(24);

        Button close = new Button();
        close.setGraphic(closeImageView);
        close.setStyle("""
            -fx-background-color: #555555; -fx-text-fill: white;
            -fx-font-weight: bold; -fx-font-size: 16px;
            -fx-max-width: 50px;  -fx-max-height: 50px;
            -fx-background-radius: 10; -fx-padding: 0;
            -fx-alignment: CENTER; -fx-border-width: 0;
            -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5), 10, 0, 0, 1);
        """);
        close.setOnAction(e -> root.getChildren().remove(this));

        // === Add everything to StackPane ===
        box.getChildren().addAll(title, topicInput, generateBtn, statusLabel, backBtn);
        getChildren().addAll(box, close);
        StackPane.setAlignment(close, Pos.TOP_RIGHT);
        StackPane.setMargin(close, new Insets(10, 14, 0, 0));

        // === GPT Generation Handling ===
        generateBtn.setOnAction(e -> {
            String topic = topicInput.getText().trim();
            if (topic.isEmpty()) {
                statusLabel.setText("⚠ Please enter a topic first!");
                return;
            }

            generateBtn.setDisable(true);
            statusLabel.setText("⏳ Generating quiz for “" + topic + "”…");

            generator.generateQuizAsync(topic, questions -> Platform.runLater(() -> {
                try {
                    if (questions == null || questions.isEmpty()) {
                        statusLabel.setText("⚠ GPT returned no questions.");
                        generateBtn.setDisable(false);
                        return;
                    }

                    String quizTitle = generator.getLastGeneratedTitle();
                    if (quizTitle == null || quizTitle.isBlank()) quizTitle = topic + " Quiz";

                    statusLabel.setText("Quiz generated: " + quizTitle + " — saving…");
                    saveGeneratedQuiz(quizTitle, questions);
                    statusLabel.setText("Saved! Returning to quiz list…");

                    root.getChildren().remove(this);
                    CreatedQuizzesView createdView = new CreatedQuizzesView(root);
                    root.getChildren().add(createdView);
                    createdView.toFront();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("Error: " + ex.getMessage());
                    generateBtn.setDisable(false);
                }
            }), error -> Platform.runLater(() -> {
                statusLabel.setText("❌ " + error);
                generateBtn.setDisable(false);
            }));
        });
    }

    private void saveGeneratedQuiz(String quizTitle, List<QuizQuestion> questions) {
        try {
            Quiz quiz = new Quiz(
                    quizTitle,
                    questions.size() > 0 ? questions.get(0).getQuestionText() : "",
                    questions.size() > 1 ? questions.get(1).getQuestionText() : "",
                    questions.size() > 2 ? questions.get(2).getQuestionText() : "",
                    questions.size() > 3 ? questions.get(3).getQuestionText() : "",
                    questions.size() > 4 ? questions.get(4).getQuestionText() : "",
                    questions.size() > 5 ? questions.get(5).getQuestionText() : "",
                    questions.size() > 6 ? questions.get(6).getQuestionText() : "",
                    questions.size() > 7 ? questions.get(7).getQuestionText() : "",
                    questions.size() > 8 ? questions.get(8).getQuestionText() : "",
                    questions.size() > 9 ? questions.get(9).getQuestionText() : ""
            );

            quizDAO.addQuiz(quiz);
            int latestQuizId = quiz.getQuizID();

            for (QuizQuestion q : questions) {
                questionDAO.addGeneratedQuestion(latestQuizId, q);
            }

            System.out.println("[CreateQuizView] Quiz '" + quizTitle + "' saved (ID=" + latestQuizId + ")");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[CreateQuizView] Error saving generated quiz: " + e.getMessage());
        }
    }
}
