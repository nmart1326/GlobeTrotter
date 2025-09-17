package com.example.globetrotter.controller;

import com.example.globetrotter.view.SidePanel;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.util.Duration;
import com.example.globetrotter.view.ToggleButtonFactory;

public class MainController {

    private SidePanel sidePanel;
    private Button toggleButton;
    private boolean isSidePanelHidden = false;
    private final int SPWidth = 400; // Match the width of the side panel
    private final String stowedIconPath;

    public MainController(SidePanel sidePanel, Button toggleButton, String stowedIconPath) {
        this.sidePanel = sidePanel;
        this.toggleButton = toggleButton;
        this.stowedIconPath = stowedIconPath;
        this.toggleButton.setOnAction(e -> toggleSidePanel());
    }

    private void toggleSidePanel() {
        TranslateTransition panelTransition = new TranslateTransition(Duration.millis(300), sidePanel.getSidePanel());
        TranslateTransition buttonTransition = new TranslateTransition(Duration.millis(300), toggleButton);

        javafx.scene.image.ImageView toggleImageView = (javafx.scene.image.ImageView) toggleButton.getGraphic();

        if (isSidePanelHidden) {
            panelTransition.setToX(0);
            buttonTransition.setToX(SPWidth - 25);
            isSidePanelHidden = false;
            sidePanel.getSidePanel().setMouseTransparent(false);
            toggleImageView.setImage(ToggleButtonFactory.createIcon("chevronRight.png"));
        } else {
            panelTransition.setToX(-SPWidth - 10);
            buttonTransition.setToX(20);
            isSidePanelHidden = true;
            sidePanel.getSidePanel().setMouseTransparent(true);
            toggleImageView.setImage(ToggleButtonFactory.createIcon(stowedIconPath));
        }
        panelTransition.play();
        buttonTransition.play();


    }
}