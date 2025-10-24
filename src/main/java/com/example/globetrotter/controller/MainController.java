package com.example.globetrotter.controller;

import com.example.globetrotter.view.*;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * MainController
 * Handles interactions for the side panel, toggle button, and quiz navigation.
 */
public class MainController {

    private final SidePanel sidePanel;
    private final Button toggleButton;
    private final StackPane root;
    private boolean isSidePanelHidden = false;
    private final int SPWidth = 400;
    private final String stowedIconPath;
    private final Map mapView;   // reference to control map input

    public MainController(SidePanel sidePanel, Button toggleButton, String stowedIconPath,
                          StackPane root, Map mapView) {
        this.sidePanel = sidePanel;
        this.toggleButton = toggleButton;
        this.stowedIconPath = stowedIconPath;
        this.root = root;
        this.mapView = mapView;

        toggleButton.setOnAction(e -> toggleSidePanel());
        sidePanel.getSignOutButton().setOnAction(e -> handleSignOut());

        // Bind quiz button inside the side panel
        for (Node node : sidePanel.getPanelChildren()) {
            if (node instanceof Button b && "Quiz".equals(b.getText())) {
                b.setOnAction(e -> openQuizView());
            }
        }
    }

    /** Sign-out handler */
    private void handleSignOut() {
        LoginView loginView = new LoginView();
        root.getChildren().add(loginView);
        loginView.toFront();
        enableMapClicks(true);
        System.out.println("[DEBUG] Signed out → returned to LoginView");
    }

    /** Opens the main Quiz Selection screen */
    private void openQuizView() {
        System.out.println("[DEBUG] Opening Quiz Selection View");

        // Remove any old instance
        root.getChildren().removeIf(node -> node instanceof QuizSelectionView);

        QuizSelectionView selectionView = new QuizSelectionView(root);
        attachOverlayBehavior(selectionView);
        root.getChildren().add(selectionView);
        selectionView.toFront();
    }

    /** Utility: enable/disable ArcGIS Map clicks */
    private void enableMapClicks(boolean enable) {
        if (mapView != null && mapView.getMapView() != null) {
            mapView.getMapView().setMouseTransparent(!enable);
            System.out.println("[DEBUG] Map clicks " + (enable ? "ENABLED" : "DISABLED"));
        }
    }

    /** Automatically disables map clicks while overlay exists, re-enables when removed */
    private void attachOverlayBehavior(StackPane overlay) {
        enableMapClicks(false);
        System.out.println("[DEBUG] Map clicks disabled for overlay: " + overlay.getClass().getSimpleName());

        // Watch for removal
        overlay.parentProperty().addListener((obs, oldParent, newParent) -> {
            if (newParent == null) {
                enableMapClicks(true);
                System.out.println("[DEBUG] Map clicks re-enabled (overlay closed)");
            }
        });
    }

    /** Toggle side panel animation */
    private void toggleSidePanel() {
        TranslateTransition panelTransition = new TranslateTransition(Duration.millis(300), sidePanel.getSidePanel());
        TranslateTransition buttonTransition = new TranslateTransition(Duration.millis(300), toggleButton);

        javafx.scene.image.ImageView toggleImageView =
                (javafx.scene.image.ImageView) toggleButton.getGraphic();

        if (isSidePanelHidden) {
            panelTransition.setToX(0);
            buttonTransition.setToX(SPWidth - 25);
            isSidePanelHidden = false;
            sidePanel.getSidePanel().setMouseTransparent(false);
            toggleImageView.setImage(ToggleButtonFactory.createIcon("chevronLeft.png"));
            System.out.println("[DEBUG] Side panel shown");
        } else {
            panelTransition.setToX(-SPWidth - 10);
            buttonTransition.setToX(20);
            isSidePanelHidden = true;
            sidePanel.getSidePanel().setMouseTransparent(true);
            toggleImageView.setImage(ToggleButtonFactory.createIcon(stowedIconPath));
            System.out.println("[DEBUG] Side panel hidden");
        }

        panelTransition.play();
        buttonTransition.play();
    }
}
