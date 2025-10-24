package com.example.globetrotter.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;


public class SidePanel {

    private int SPWidth =  400; //Side Panel Width
    private final VBox sideNavContent;

    public SidePanel(){
        this.sideNavContent = createSideNavContent();
    }

    public VBox getSideNavContent(){
        return this.sideNavContent;
    }
    public Region getSidePanel() { return sideNavContent; }

    private VBox createSideNavContent(){

        VBox panel = new VBox();
        panel.setPrefWidth(SPWidth);
        panel.setMaxWidth(SPWidth);
        panel.setStyle("-fx-background-color: #797979;" +
                        " -fx-padding: 10; -fx-spacing: 10;" +
                        " -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5), 5, 0, 5, 0);");
        panel.setAlignment(Pos.TOP_CENTER);

        Label navTitle = new Label("GlobeTrotter");
        navTitle.setId("app-title");

        Button homeButton = new Button("Sign Out");
        Button quizButton = new Button("Quiz");
        Button settingsButton = new Button("Settings");

        panel.getChildren().addAll(navTitle, homeButton, quizButton, settingsButton);



        InfoPanel infoPanel = new InfoPanel();
        VBox.setVgrow(infoPanel, Priority.ALWAYS);
        panel.getChildren().add(infoPanel);

        return panel;
    }

    public Button getSignOutButton() {
        VBox panel = sideNavContent;
        for (javafx.scene.Node node : panel.getChildren()) {
            if (node instanceof Button b && "Sign Out".equals(b.getText())) {
                return b;
            }
        }
        return null;
    }

    public InfoPanel getInfoPanel() {
        for (javafx.scene.Node node : sideNavContent.getChildren()) {
            if (node instanceof InfoPanel infoPanel) {
                return infoPanel;
            }
        }
        return null;
    }

    // Fraser - Added helper method to expose panel children safely
    public List<javafx.scene.Node> getPanelChildren() {
        return new ArrayList<>(sideNavContent.getChildren());
    }

}
