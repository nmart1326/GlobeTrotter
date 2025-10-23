package com.example.globetrotter.view;

import com.example.globetrotter.controller.QuizController;
import com.example.globetrotter.model.QuizQuestion;
import com.example.globetrotter.model.SqliteQuizQuestionDAO;
import com.example.globetrotter.service.QuizService;
import com.example.globetrotter.util.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.List;

public class QuizView extends StackPane {

    private Runnable onBack;
    public void setOnBack(Runnable r) { this.onBack = r; }

    // Core quiz data
    private List<QuizQuestion> questions;
    private int currentIndex = 0;
    private int score = 0;

    // UI elements
    private Label scoreLabel;
    private Label questionLabel;
    private ToggleButton a, b, c, d;
    private Button next;
    private ToggleGroup group;
    private VBox content;
    private StackPane card;

    public QuizView(QuizController controller) {
        this.onBack = onBack;

        QuizService quizService = new QuizService();
        SqliteQuizQuestionDAO dao = new SqliteQuizQuestionDAO();
        questions = dao.getQuestionsForQuiz(1);

        // Load close icon
        Image closeImage = new Image(getClass().getResourceAsStream("/assets/icons/close.png"));
        ImageView closeImageView = new ImageView(closeImage);
        closeImageView.setFitWidth(24);
        closeImageView.setFitHeight(24);

        // Close button
        Button close = new Button();
        close.setGraphic(closeImageView);
        close.setStyle("-fx-background-color: #555555; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-font-size: 16px;" +
                "-fx-max-width: 50px;  -fx-max-height: 50px;" +
                " -fx-background-radius: 10; -fx-padding: 0;" +
                " -fx-alignment: CENTER; -fx-border-width: 0;" +
                " -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5), 10, 0, 0, 1);");
        close.setOnAction(e -> ((StackPane) getParent()).getChildren().remove(this));

        // Card background
        card = new StackPane();
        card.setMaxWidth(760);
        card.setMaxHeight(520);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: #E5E7EB;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0.25, 0, 6);
        """);

        // Score label
        scoreLabel = new Label("Score: 0 / 10");
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Question label
        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-size:18px; -fx-font-weight:700;");

        // Answer grid
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        group = new ToggleGroup();
        a = option("Option 1", group);
        b = option("Option 2", group);
        c = option("Option 3", group);
        d = option("Option 4", group);

        grid.add(a, 0, 0);
        grid.add(b, 1, 0);
        grid.add(c, 0, 1);
        grid.add(d, 1, 1);

        next = new Button("Next");
        next.setDisable(true);
        next.setStyle("-fx-background-color: #555555; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-font-size: 16px;" +
                "-fx-max-width: 150px;  -fx-max-height: 50px;" +
                "-fx-min-width: 150px;  -fx-min-height: 50px;" +
                " -fx-background-radius: 10; -fx-padding: 0;" +
                " -fx-alignment: CENTER; -fx-border-width: 0;" +
                " -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5), 10, 0, 0, 1);");

        HBox bottomBar = new HBox(next);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        content = new VBox(16, scoreLabel, questionLabel, grid, bottomBar);
        content.setPadding(new Insets(24));
        content.setFillWidth(true);

        card.getChildren().add(content);
        StackPane.setAlignment(close, Pos.TOP_RIGHT);
        StackPane.setMargin(close, new Insets(10, 14, 0, 0));
        card.getChildren().add(close);

        setAlignment(Pos.CENTER);
        getChildren().add(card);
        setPadding(new Insets(20));

        // Load first question
        if (!questions.isEmpty()) {
            loadQuestion();
        } else {
            questionLabel.setText("No quiz data available!");
        }

        // Handle answer selection
        group.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT != null) {
                next.setDisable(false);
                ToggleButton btn = (ToggleButton) newT;
                checkAnswer(btn);
            }
        });

        // Next button click → move to next question
        next.setOnAction(e -> nextQuestion());

        // ESC key closes quiz
        setFocusTraversable(true);
        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                ((StackPane) getParent()).getChildren().remove(this);
            }
        });
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) requestFocus();
        });
    }

    private void loadQuestion() {
        QuizQuestion q = questions.get(currentIndex);
        questionLabel.setText((currentIndex + 1) + ". " + q.getQuestionText());
        a.setText(q.getOptionA());
        b.setText(q.getOptionB());
        c.setText(q.getOptionC());
        d.setText(q.getOptionD());
        group.selectToggle(null);
        resetButtonStyle(a, b, c, d);
        next.setDisable(true);
    }

    private void checkAnswer(ToggleButton selected) {
        QuizQuestion q = questions.get(currentIndex);
        String correctText = switch (q.getCorrectOption()) {
            case "A" -> q.getOptionA();
            case "B" -> q.getOptionB();
            case "C" -> q.getOptionC();
            case "D" -> q.getOptionD();
            default -> "";
        };

        if (selected.getText().equals(correctText)) {
            selected.setStyle("-fx-background-color: #a8f0a1; -fx-background-radius: 12; -fx-border-radius: 12;");
            score++;
        } else {
            selected.setStyle("-fx-background-color: #f0a1a1; -fx-background-radius: 12; -fx-border-radius: 12;");
        }

        // Disable other buttons
        for (Toggle t : group.getToggles()) ((ToggleButton) t).setDisable(true);

        scoreLabel.setText("Score: " + score + " / " + questions.size());
    }

    private void nextQuestion() {
        currentIndex++;
        if (currentIndex < questions.size()) {
            loadQuestion();
        } else {
            showResults();
        }
    }

    private void showResults() {
        // Save the user's score in the database
        int currentUserId = UserSession.getInstance().getUserId();
        if (currentUserId == -1) {
            System.err.println("No logged-in user found. Defaulting to guest (UserID=1).");
            currentUserId = 1;
        }
        int quizId = 1;         // Currently you’re running Australian quiz (QuizID = 1)
        new com.example.globetrotter.service.ScoreService().saveScore(currentUserId, quizId, score);

        content.getChildren().clear();
        Label result = new Label("🎉 Quiz Complete!\nYour score: " + score + " / " + questions.size());
        result.setStyle("-fx-font-size:20px; -fx-font-weight:bold; -fx-text-fill:#111827;");

        Button restart = new Button("Restart Quiz");
        restart.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-font-size: 16px;" +
                "-fx-max-width: 180px;  -fx-max-height: 50px;" +
                " -fx-background-radius: 10; -fx-padding: 10 20;" +
                " -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.4), 10, 0, 0, 1);");

        restart.setOnAction(e -> restartQuiz());

        VBox box = new VBox(20, result, restart);
        box.setAlignment(Pos.CENTER);
        content.getChildren().clear();
        content.getChildren().add(box);
    }


    private void restartQuiz() {
        // Reset internal variables (for consistency)
        score = 0;
        currentIndex = 0;

        // Remove the current quiz view from parent
        StackPane parent = (StackPane) getParent();
        if (parent != null) {
            parent.getChildren().remove(this);

            // Create a fresh quiz controller and view (same as new quiz start)
            com.example.globetrotter.controller.QuizController newController =
                    new com.example.globetrotter.controller.QuizController(
                            new com.example.globetrotter.service.QuizService());
            com.example.globetrotter.view.QuizView newQuizView =
                    new com.example.globetrotter.view.QuizView(newController);

            parent.getChildren().add(newQuizView);
        } else {
            System.err.println("Restart failed: QuizView has no parent StackPane.");
        }
    }


    private ToggleButton option(String text, ToggleGroup group) {
        ToggleButton b = new ToggleButton(text);
        b.setToggleGroup(group);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMinHeight(56);
        GridPane.setHgrow(b, Priority.ALWAYS);
        b.setStyle("""
            -fx-background-color: #F8FAFC;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: #D1D5DB;
            -fx-border-width: 1;
            -fx-font-size: 14px;
            -fx-font-weight: 600;
            -fx-padding: 10 14 10 14;
        """);
        return b;
    }

    private void resetButtonStyle(ToggleButton... buttons) {
        for (ToggleButton b : buttons) {
            b.setStyle("""
                -fx-background-color: #F8FAFC;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-color: #D1D5DB;
                -fx-border-width: 1;
                -fx-font-size: 14px;
                -fx-font-weight: 600;
                -fx-padding: 10 14 10 14;
            """);
            b.setDisable(false);
        }
    }
}
