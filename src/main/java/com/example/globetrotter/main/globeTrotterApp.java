package com.example.globetrotter.main;

import com.example.globetrotter.controller.MainController;
import com.example.globetrotter.view.SidePanel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.example.globetrotter.view.Map;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import com.example.globetrotter.view.ToggleButtonFactory;
import javafx.scene.control.Button;

public class globeTrotterApp extends Application {

    private Map mapView;
    private SidePanel sidePanel;
    private Button toggleButton;
    private boolean isSidePanelHidden = false;
    private final int SPWidth = 400;

    @Override
    public void start(Stage primaryStage) {
        // Loads icon on the title bar of the window, and taskbar
        try{
            Image icon = new Image(getClass().getResourceAsStream("/assets.icons/globetrotter.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e){
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        // Initialize from com/example/globetrotter/view/
        mapView = new Map();
        sidePanel = new SidePanel();
        toggleButton = ToggleButtonFactory.createButton();
        // Initialize the version label
        Label versionLabel = new Label("Globetrotter v0.1.0");


        // UI Layers from Bottom->Top: {versionLabel✓, map✓, QuizTablet, NavigationsidePanel✓, LoginScreen}
        StackPane root = new StackPane();
        root.getChildren().addAll(versionLabel, mapView.getMapView(), sidePanel.getSidePanel(), toggleButton);

        StackPane.setAlignment(sidePanel.getSidePanel(), Pos.CENTER_LEFT);
        StackPane.setAlignment(toggleButton, Pos.TOP_LEFT);

        toggleButton.setTranslateX(SPWidth - 25);
        toggleButton.setTranslateY(70);

        new MainController(sidePanel, toggleButton, "home.png");


        Scene scene = new Scene(root, 1500, 750);
        //Load Fonts
        scene.getStylesheets().add(getClass().getResource("/com/example/globetrotter/styles.css").toExternalForm());


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