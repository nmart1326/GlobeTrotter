package com.example.globetrotter.view;

import com.example.globetrotter.controller.QuizController;
import com.example.globetrotter.service.QuizService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Displays the selection screen for quiz options.
 */
public class QuizSelectionView extends StackPane {

    private final StackPane root;

    public QuizSelectionView(StackPane root) {
        this.root = root;

        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgba(255,255,255,0.01);");

        // === Main white panel ===
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: #E5E7EB;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0.25, 0, 6);
            -fx-padding: 40;
        """);

        Label title = new Label("Quiz Selection");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        // === Buttons ===
        Button tutorialQuizBtn = new Button("Tutorial: Australia Quiz");
        Button createQuizBtn = new Button("Create a Quiz");
        Button viewCreatedBtn = new Button("View Created Quizzes");
        Button accessClassQuizBtn = new Button("Access Class Quiz");

        String btnStyle = """
            -fx-background-color: #2563EB;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 12 24;
            -fx-max-width: 260px;
            -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.4), 10, 0, 0, 1);
        """;
        tutorialQuizBtn.setStyle(btnStyle);
        createQuizBtn.setStyle(btnStyle.replace("#2563EB", "#16A34A"));
        viewCreatedBtn.setStyle(btnStyle.replace("#2563EB", "#0EA5E9"));
        accessClassQuizBtn.setStyle(btnStyle.replace("#2563EB", "#9333EA"));

        layout.getChildren().addAll(title, tutorialQuizBtn, createQuizBtn, viewCreatedBtn, accessClassQuizBtn);

        // === Close button (top-right corner) ===
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

        // === Add layout + close button to the same StackPane ===
        getChildren().addAll(layout, close);
        StackPane.setAlignment(close, Pos.TOP_RIGHT);
        StackPane.setMargin(close, new Insets(10, 14, 0, 0));

        // === Button Actions ===
        tutorialQuizBtn.setOnAction(e -> {
            try {
                root.getChildren().remove(this);
                QuizController controller = new QuizController(new QuizService());
                QuizView quizView = new QuizView(controller, root);
                root.getChildren().add(quizView);
                quizView.toFront();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        createQuizBtn.setOnAction(e -> {
            try {
                root.getChildren().remove(this);
                CreateQuizView createView = new CreateQuizView(root);
                root.getChildren().add(createView);
                createView.toFront();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        viewCreatedBtn.setOnAction(e -> {
            try {
                root.getChildren().remove(this);
                CreatedQuizzesView createdView = new CreatedQuizzesView(root);
                root.getChildren().add(createdView);
                createdView.toFront();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        accessClassQuizBtn.setOnAction(e -> {
            System.out.println("[INFO] Access Class Quiz clicked (not implemented)");
        });
    }
}
