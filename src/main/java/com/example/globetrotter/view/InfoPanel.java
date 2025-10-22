package com.example.globetrotter.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import com.example.globetrotter.service.GeoInfoService;


// Panel located in SidePanel to display info about geographic locations
public class InfoPanel extends VBox {

    private final Label titleLabel;
    private final TextArea outputArea;
    private final TextField inputField;
    private final Button askButton;
    private final GeoInfoService geoInfoService = new GeoInfoService();


    public InfoPanel() {
        // Overall layout setup
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_CENTER);
        setStyle("""
            -fx-background-color: #5e5e5e;
            -fx-border-color: #999;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
        """);

        // Title
        titleLabel = new Label("Geo Assistant");
        titleLabel.setFont(Font.font("Segoe UI Semibold", 16));
        titleLabel.setStyle("-fx-text-fill: white;");
        VBox.setMargin(titleLabel, new Insets(0, 0, 4, 0));

        // Clear button beside title
        Button clearButton = new Button("Clear");
        clearButton.setStyle("""
            -fx-background-color: #777;
            -fx-text-fill: white;
            -fx-font-size: 11px;
            -fx-background-radius: 5;
            -fx-padding: 2 6 2 6;
        """);
        clearButton.setOnAction(e -> clear());
        HBox titleBar = new HBox(10, titleLabel, clearButton);
        titleBar.setAlignment(Pos.CENTER_LEFT);

        // Output area (for results or clicked-place info)
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefHeight(180);
        outputArea.setStyle("""
            -fx-control-inner-background: #f4f4f4;
            -fx-text-fill: #222;
            -fx-background-radius: 6;
            -fx-border-color: #ccc;
        """);

        // Wrap the TextArea in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(outputArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // make ScrollPane fill available space
        scrollPane.setPrefViewportHeight(300); // sets initial visible area height


        // Input field and button area
        inputField = new TextField();
        inputField.setPromptText("Ask about any place (e.g., 'What is Sydney known for?')");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        askButton = new Button("Ask");
        askButton.setOnAction(e -> handleAsk());

        HBox inputArea = new HBox(8, inputField, askButton);
        inputArea.setAlignment(Pos.CENTER_RIGHT);

        // Add everything to the panel
        getChildren().addAll(titleBar, scrollPane, inputArea);
    }


    //Answer - will be triggered when user clicks ask button - for now echoes question
    //Will connect to GeoInfoService
    private void handleAsk() {
        String question = inputField.getText().trim();
        if (question.isEmpty()) return;

        clear();
        appendText("> " + question);
        appendText("Thinking...\n");
        inputField.clear();

        // --- call GeoInfoService asynchronously ---
        geoInfoService.askQuestionAsync(question, response ->
                javafx.application.Platform.runLater(() -> {
                    clear(); // remove "Thinking..."
                    appendText("> " + question);
                    appendText(response);
                })
        );
        // --- end call ---
    }



    // Called externally to display information about a clicked place
    public void displayInfo(String placeName, String summary) {
        appendText("📍 " + placeName + "\n" + summary + "\n");
    }

    // Utility to write to output area
    private void appendText(String text) {
        outputArea.appendText(text + "\n");
    }

    // Clears text output - optional use later
    public void clear() {
        outputArea.clear();
    }
}
