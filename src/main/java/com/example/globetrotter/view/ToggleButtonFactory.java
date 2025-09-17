package com.example.globetrotter.view;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


public class ToggleButtonFactory {

    public static Button createButton() {
        Button button = new Button();
        try {
            Image icon = new Image(ToggleButtonFactory.class.getResourceAsStream("/assets.icons/chevronRight.png"));
            ImageView toggleImageView = new ImageView(icon);

            toggleImageView.setFitWidth(20);
            toggleImageView.setFitHeight(20);
            button.setGraphic(toggleImageView);
        } catch (Exception e) {
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        button.setStyle("-fx-background-color: #555555; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-font-size: 16px;" +
                "-fx-max-width: 50px;  -fx-max-height: 50px;" +
                " -fx-background-radius: 10; -fx-padding: 0;" +
                " -fx-alignment: CENTER; -fx-border-width: 0;" +
                " -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5), 10, 0, 0, 1);");
        return button;
    }
    public static Image createIcon(String iconName){
        return new Image(ToggleButtonFactory.class.getResourceAsStream("/assets.icons/"+iconName));
    }

    public static ImageView createButtonIcon(String iconPath){
        ImageView iconView = new ImageView((createIcon(iconPath)));
        iconView.setFitWidth(20);
        iconView.setFitHeight(20);
        return iconView;
    }

}
