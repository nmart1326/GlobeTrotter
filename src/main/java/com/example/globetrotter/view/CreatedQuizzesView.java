package com.example.globetrotter.view;

import com.example.globetrotter.controller.QuizController;
import com.example.globetrotter.model.Quiz;
import com.example.globetrotter.model.SqliteQuizDAO;
import com.example.globetrotter.service.QuizService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.util.List;

/**
 * Displays all stored quizzes, each clickable to start a QuizView.
 */
public class CreatedQuizzesView extends StackPane {

    private final SqliteQuizDAO quizDAO = new SqliteQuizDAO();
    private VBox quizList;
    private final StackPane root;

    public CreatedQuizzesView(StackPane root) {
        this.root = root;

        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgba(255,255,255,0.01);");

        // === Main white card ===
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: #E5E7EB;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0.25, 0, 6);
            -fx-padding: 40;
        """);

        Label title = new Label("Created Quizzes");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");


        quizList = new VBox(12);
        quizList.setAlignment(Pos.CENTER);
        quizList.setFillWidth(false);
        refreshQuizList();
        scrollPane.setContent(quizList);





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

        mainBox.getChildren().addAll(title, scrollPane, backBtn);

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

        // === Add both to StackPane ===
        getChildren().addAll(mainBox, close);
        StackPane.setAlignment(close, Pos.TOP_RIGHT);
        StackPane.setMargin(close, new Insets(10, 14, 0, 0));
    }

    private void refreshQuizList() {
        quizList.getChildren().clear();
        List<Quiz> allQuizzes = quizDAO.getAllQuizzes();

        if (allQuizzes.isEmpty()) {
            Label noQuiz = new Label("No quizzes found. Create one first!");
            noQuiz.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
            quizList.getChildren().add(noQuiz);
        } else {
            for (Quiz quiz : allQuizzes) {
                String titleText = "Quiz #" + quiz.getQuizID() + " — " + summarizeTitle(quiz);
                Button quizBtn = new Button(titleText);

                quizBtn.setMinWidth(400);
                quizBtn.setMaxWidth(Region.USE_PREF_SIZE);
                quizBtn.setWrapText(false);
                VBox.setVgrow(quizBtn, Priority.NEVER);

                quizBtn.setStyle("""
                    -fx-background-color: #2563EB;
                    -fx-text-fill: white;
                    -fx-font-size: 15px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 10;
                    -fx-padding: 12 20;
                """);

                quizBtn.setOnAction(e -> {
                    root.getChildren().remove(this);
                    QuizController controller = new QuizController(new QuizService());
                    QuizView quizView = new QuizView(controller, root);
                    quizView.loadQuizById(quiz.getQuizID());
                    root.getChildren().add(quizView);
                    quizView.toFront();
                });

                quizList.getChildren().add(quizBtn);
            }

        }
    }

    private String summarizeTitle(Quiz quiz) {
        try {
            var field = Quiz.class.getDeclaredField("title");
            field.setAccessible(true);
            Object title = field.get(quiz);
            if (title != null && !title.toString().isBlank()) {
                return title.toString();
            }
        } catch (Exception ignore) { }

        String q = quiz.getQ1();
        if (q == null || q.isEmpty()) return "(Untitled Quiz)";
        q = q.length() > 50 ? q.substring(0, 50) + "…" : q;
        return q;
    }
}
