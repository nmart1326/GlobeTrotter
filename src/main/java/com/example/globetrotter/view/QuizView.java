package com.example.globetrotter.view;

import com.example.globetrotter.controller.QuizController;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

// Must extend a JavaFX Node/Parent so it can be added to the scene graph
public class QuizView extends StackPane {
    public QuizView(QuizController controller) {
        getChildren().add(new Label("Quiz screen"));
    }
}