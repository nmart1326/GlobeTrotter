package com.example.globetrotter.main;

import com.example.globetrotter.view.Map;
import com.example.globetrotter.view.QuizView;
import com.example.globetrotter.controller.QuizController;
import com.example.globetrotter.service.QuizService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class globeTrotterApp extends Application {

    private Map mapView;

    @Override
    public void start(Stage primaryStage) {
        // Loads icon on the title bar of the window, and taskbar
        try{
            Image icon = new Image(getClass().getResourceAsStream("/assets.icons/globetrotter.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e){
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        // Initialize the map from com/example/globetrotter/view/Map.java
        mapView = new Map();

        // Welcome label
        Label versionLabel = new Label("Globetrotter v0.1.0");

        // UI Layers from Bottom->Top: {versionLabel✓, map✓, QuizTablet, NavigationsidePanel, LoginScreen}
        StackPane root = new StackPane();
        root.getChildren().addAll(versionLabel, mapView.getMapView());

        // Quiz Button
        Button startQuizBtn = new Button("Start Quiz");
        HBox topRight = new HBox(startQuizBtn);
        topRight.setAlignment(Pos.TOP_RIGHT);
        topRight.setPadding(new Insets(10));

        root.getChildren().add(topRight);
        StackPane.setAlignment(topRight, Pos.TOP_RIGHT);

        startQuizBtn.setOnAction(e -> {
            var controller = new QuizController(new QuizService());
            var quizView   = new QuizView(controller);
            quizView.setOnBack(() -> root.getChildren().remove(quizView));
            root.getChildren().add(quizView);
        });

        // Ensure map is still clickable
        versionLabel.setMouseTransparent(true);
        topRight.setPickOnBounds(false);

        Scene scene = new Scene(root, 1000, 750);

        // Window Label
        primaryStage.setTitle("Globetrotter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Clean up Map Resources
        if (mapView != null) {
            mapView.dispose();
        }
    }

    public static void main(String[] args) {
        launch(args);  // starts the JavaFX application
    }
}