package com.example.globetrotter.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import com.example.globetrotter.service.GeoInfoService;

// Panel located in SidePanel to display info about geographic locations
public class InfoPanel extends VBox {

    private final Label titleLabel;
    private final TextField inputField;
    private final Button askButton;
    private final GeoInfoService geoInfoService = new GeoInfoService();
    private VBox chatContainer;
    private ScrollPane scrollPane;

    public InfoPanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_CENTER);
        setStyle("""
                -fx-background-color: #5e5e5e;
                -fx-border-color: #999;
                -fx-border-radius: 6;
                -fx-background-radius: 6;
                """);

        Image trotBot = new Image(getClass().getResourceAsStream("/assets/icons/trotBot.png"));
        ImageView trotBotImageView = new ImageView(trotBot);
        trotBotImageView.setFitWidth(32);
        trotBotImageView.setFitHeight(32);

        titleLabel = new Label("TrotBot", trotBotImageView);
        titleLabel.setGraphicTextGap(10);
        titleLabel.setContentDisplay(ContentDisplay.LEFT);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setFont(Font.font("Segoe UI Semibold", 16));
        VBox.setMargin(titleLabel, new Insets(0, 0, 4, 0));

        Image eraserImage = new Image(getClass().getResourceAsStream("/assets/icons/eraser.png"));
        ImageView eraserView = new ImageView(eraserImage);
        eraserView.setFitHeight(24);
        eraserView.setFitWidth(24);

        Button clearButton = new Button();
        clearButton.setGraphic(eraserView);
        clearButton.setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #2196F3, #9C27B0);
                -fx-border-color: transparent;
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
                -fx-background-radius: 10;
                -fx-padding: 9;
                -fx-cursor: hand;
                """);
        Tooltip.install(clearButton, new Tooltip("Clear"));
        clearButton.setOnAction(e -> clear());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleBar = new HBox(10, titleLabel, spacer, clearButton);
        titleBar.setAlignment(Pos.CENTER_LEFT);

        scrollPane = new ScrollPane();
        chatContainer = new VBox(6);
        scrollPane.setContent(chatContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("""
                -fx-background-color: transparent;
                -fx-border-color: transparent;
                """);
        scrollPane.setPrefViewportHeight(300);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(24);
        clip.setArcHeight(24);
        scrollPane.setClip(clip);
        scrollPane.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        inputField = new TextField();
        inputField.setPrefHeight(45);
        inputField.setStyle("""
                -fx-background-color: #ffffff;
                -fx-control-inner-background: #ffffff;
                -fx-border-color: #d0d0d0;
                -fx-border-width: 0.25;
                -fx-border-radius: 12;
                -fx-background-radius: 12;
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
                -fx-padding: 0 6 0 6;
                -fx-font-size: 13px;
                """);
        inputField.setPromptText("Ask about any places travel, places, geography");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        inputField.setOnAction(e -> handleAsk());

        Image sendImage = new Image(getClass().getResourceAsStream("/assets/icons/send.png"));
        ImageView sendView = new ImageView(sendImage);
        sendView.setFitHeight(24);
        sendView.setFitWidth(24);

        askButton = new Button();
        askButton.setGraphic(sendView);
        askButton.setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #2196F3, #9C27B0);
                -fx-border-color: transparent;
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
                -fx-background-radius: 10;
                -fx-padding: 9;
                -fx-cursor: hand;
                """);
        askButton.setOnAction(e -> handleAsk());

        HBox inputArea = new HBox(8, inputField, askButton);
        inputArea.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(titleBar, scrollPane, inputArea);
    }

    private void addMessage(String text, boolean isUser) {
        Label msgLabel = new Label(text);
        msgLabel.setWrapText(true);
        msgLabel.setPadding(new Insets(10));           // space inside bubble
        msgLabel.setStyle(isUser
                ? "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 12 12 0 12;"
                : "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 12 12 12 0;");

        // Restrict width for wrapping
        msgLabel.setMaxWidth(250);
        // Optional: dynamically adapt to scrollPane width
        // msgLabel.maxWidthProperty().bind(scrollPane.widthProperty().subtract(40));

        HBox msgBox = new HBox(msgLabel);
        msgBox.setPadding(new Insets(5, 10, 5, 10));  // space between bubble and scrollpane border
        msgBox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        chatContainer.getChildren().add(msgBox);

        // Scroll to bottom
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }





    private void handleAsk() {
        String question = inputField.getText().trim();
        if (question.isEmpty()) return;


        addMessage(question, true);
        addMessage("Thinking...", false);
        inputField.clear();

        geoInfoService.askQuestionAsync(question, response ->
                javafx.application.Platform.runLater(() -> {
                    if (!chatContainer.getChildren().isEmpty()) {
                        chatContainer.getChildren().remove(chatContainer.getChildren().size() - 1);
                    }
                    addMessage(response, false);
                })
        );
    }

    public void displayInfo(String placeName, String summary) {
        addMessage("📍 " + placeName + "\n" + summary, false);
    }

    public void clear() {
        if (chatContainer != null) {
            chatContainer.getChildren().clear();
        }
    }
}
