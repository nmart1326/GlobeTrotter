// CloseButtonFactory.java
package com.example.globetrotter.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CloseButtonFactory {

    public static Button create(StackPane root, StackPane parent) {
        Image icon = new Image(CloseButtonFactory.class.getResourceAsStream("/assets/icons/close.png"));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(24);
        iconView.setFitHeight(24);

        Button close = new Button();
        close.setGraphic(iconView);
        close.setStyle("""
            -fx-background-color: #555555; -fx-text-fill: white;
            -fx-font-weight: bold; -fx-font-size: 16px;
            -fx-max-width: 50px; -fx-max-height: 50px;
            -fx-background-radius: 10; -fx-padding: 0;
            -fx-alignment: CENTER; -fx-border-width: 0;
            -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5), 10, 0, 0, 1);
        """);

        close.setOnAction(e -> root.getChildren().remove(parent));

        StackPane.setAlignment(close, Pos.TOP_RIGHT);
        StackPane.setMargin(close, new Insets(10, 14, 0, 0));
        return close;
    }
}
