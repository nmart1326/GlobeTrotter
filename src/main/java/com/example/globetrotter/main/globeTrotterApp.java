package com.example.globetrotter.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class globeTrotterApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Loads icon on the title bar of the window, and taskbar
        try{
            Image icon = new Image(getClass().getResourceAsStream("/assets.icons/globetrotter.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e){
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        Label welcomeLabel = new Label("Globetrotter v0.1.0");

        StackPane root = new StackPane();
        root.getChildren().add(welcomeLabel);


        Scene scene = new Scene(root, 1000, 750);

        primaryStage.setTitle("Globetrotters");  //Sets title of the primary stage

        primaryStage.setScene(scene);  //Sets scene on the primary stage

        primaryStage.show();  //Shows stage to user
    }

    public static void main(String[] args) {
        launch(args);  // starts the JavaFX application
    }
}