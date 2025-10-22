package com.example.globetrotter.main;

import com.example.globetrotter.controller.MainController;
import com.example.globetrotter.controller.QuizController;
import com.example.globetrotter.service.QuizService;
import com.example.globetrotter.view.LoginView;
import com.example.globetrotter.view.Map;
import com.example.globetrotter.view.QuizView;
import com.example.globetrotter.view.SidePanel;
import com.example.globetrotter.view.ToggleButtonFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.nio.file.Path;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;

public class globeTrotterApp extends Application {

    private Map mapView;
    private SidePanel sidePanel;
    private Button toggleButton;
    private boolean isSidePanelHidden = false;
    private final int SPWidth = 400;
    private LoginView loginView;

    @Override
    public void start(Stage primaryStage) {
        Path installDir = Path.of("target", "arcgisruntime").toAbsolutePath();
        ArcGISRuntimeEnvironment.setInstallDirectory(installDir.toString());

        // Loads icon on the title bar of the window, and taskbar
        try{
            Image icon = new Image(getClass().getResourceAsStream("/assets/icons/globetrotter.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e){
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        // Initialize from com/example/globetrotter/view/
        sidePanel = new SidePanel();
        var infoPanel = sidePanel.getInfoPanel();
        mapView = new Map(infoPanel);
        toggleButton = ToggleButtonFactory.createButton();
        loginView = new LoginView();

        // Initialize the version label
        Label versionLabel = new Label("Globetrotter v0.1.0");


        // UI Layers from Bottom->Top: {versionLabel✓, map✓, QuizTablet, NavigationsidePanel✓, LoginScreen}
        StackPane root = new StackPane();
        root.getChildren().addAll(versionLabel, mapView.getMapView(), sidePanel.getSidePanel(), toggleButton);


        StackPane.setAlignment(sidePanel.getSidePanel(), Pos.CENTER_LEFT);
        StackPane.setAlignment(toggleButton, Pos.TOP_LEFT);

        toggleButton.setTranslateX(SPWidth - 25);
        toggleButton.setTranslateY(70);

        new MainController(sidePanel, toggleButton, "home.png", root);


        Scene scene = new Scene(root, 1280, 720);
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