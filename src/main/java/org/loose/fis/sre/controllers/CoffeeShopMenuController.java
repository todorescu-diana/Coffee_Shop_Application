package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CoffeeShopMenuController {

    @FXML
    private VBox verticalBoxContainer;

    @FXML
    private void handleButtonAction(javafx.event.ActionEvent event) {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"));
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(new Text("Nume"), new Text("Descriere"), new Text("volum"));

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane("New Pane", newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button newEditButton = new Button("Edit");
        Button newDeleteButton = new Button("Delete");
        newHBox.getChildren().addAll(newTitledPane, newEditButton, newDeleteButton);
        verticalBoxContainer.getChildren().add(newHBox);
    }

}
