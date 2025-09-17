package com.example.globetrotter.view;

import com.example.globetrotter.controller.QuizController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class QuizView extends StackPane {

    private Runnable onBack;
    public void setOnBack(Runnable r) { this.onBack = r; }

    public QuizView(QuizController controller) {
        this.onBack = onBack;

        // back button on invisible backbar
        HBox topBar = new HBox(8);
        Button back = new Button("Back");
        back.setOnAction(e -> {
            if (onBack != null) onBack.run();
            else ((StackPane) getParent()).getChildren().remove(this);
        });
        topBar.getChildren().add(back);
        StackPane.setAlignment(topBar, Pos.TOP_LEFT);
        StackPane.setMargin(topBar, new Insets(12));
        getChildren().add(topBar);

        // rectangle background
        StackPane card = new StackPane();
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

        VBox content = new VBox(16);
        content.setPadding(new Insets(24));
        content.setFillWidth(true);

        Label question = new Label("Which city is the capital of Australia?");
        question.setStyle("-fx-font-size:18px; -fx-font-weight:700;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        ToggleGroup group = new ToggleGroup();
        ToggleButton a = option("Sydney", group);
        ToggleButton b = option("Canberra", group);
        ToggleButton c = option("Melbourne", group);
        ToggleButton d = option("Brisbane", group);

        grid.add(a, 0, 0);
        grid.add(b, 1, 0);
        grid.add(c, 0, 1);
        grid.add(d, 1, 1);

        Button next = new Button("Next");
        next.setDisable(true);
        group.selectedToggleProperty().addListener((obs, oldT, newT) -> next.setDisable(newT == null));
        HBox bottomBar = new HBox(next);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(question, grid, bottomBar);
        card.getChildren().add(content);

        setAlignment(Pos.CENTER);
        getChildren().add(card);
        setPadding(new Insets(20));
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
        b.selectedProperty().addListener((o, was, is) -> {
            b.setStyle(is ? """
                -fx-background-color: #EEF6FF;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-color: #2563EB;
                -fx-border-width: 2;
                -fx-font-size: 14px;
                -fx-font-weight: 700;
                -fx-padding: 10 14 10 14;
            """ : """
                -fx-background-color: #F8FAFC;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-color: #D1D5DB;
                -fx-border-width: 1;
                -fx-font-size: 14px;
                -fx-font-weight: 600;
                -fx-padding: 10 14 10 14;
            """);
        });
        return b;
    }
}