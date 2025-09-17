package com.example.globetrotter.view;

import com.example.globetrotter.controller.QuizController;
import com.example.globetrotter.service.QuizService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public final class MainApplicationView extends BorderPane {

    private final Map mapViewWrapper = new Map();

    public MainApplicationView() {
        setCenter(mapViewWrapper.getMapView());

        // Top bar with the quiz button
        Button startQuizBtn = new Button("Start Quiz");
        HBox topBar = new HBox(10, startQuizBtn);
        topBar.setPadding(new Insets(10));
        setTop(topBar);


        // Navigation: replace center with the quiz screen
        startQuizBtn.setOnAction(e -> {
            var controller = new QuizController(new QuizService()); // or a stub for now
            var quizView   = new QuizView(controller);
            setCenter(quizView);
        });
    }
}