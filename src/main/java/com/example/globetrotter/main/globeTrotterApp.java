package com.example.globetrotter.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.example.globetrotter.view.Map;
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