package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.model.CoffeeShop;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.loose.fis.sre.services.CoffeeShopService.getCoffeeShopsRepository;

public class CoffeeShopListController {
    @FXML
    private VBox verticalBoxContainer;

    static String selectedCoffeeShopName;

    public void initialize () {
        ObjectRepository<CoffeeShop> coffeeShopsRepository = getCoffeeShopsRepository();

        for(CoffeeShop shop: coffeeShopsRepository.find()) {
            createNewShopContainer(shop.getName());
        }
    }

    public void createNewShopContainer(String name) {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        Text nameField = new Text(name);

        newVBoxTitles.getChildren().add(new Text("Name:"));
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().add(nameField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane(name, newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button goToMenuButton = new Button("Go to coffee shop's menu");

        goToMenuButton.setOnAction((event) -> {
            Stage currentStage = (Stage) goToMenuButton.getScene().getWindow();

            currentStage.close();
            selectedCoffeeShopName = name;
            Parent coffeeShopList = null;
            try {
                coffeeShopList = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeShopMenuClient.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene newScene = new Scene(coffeeShopList);
            Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newStage.setScene(newScene);
            newStage.show();
        });
        newHBox.getChildren().addAll(newTitledPane, goToMenuButton);

        verticalBoxContainer.getChildren().add(newHBox);
    }

    public static String getSelectedCoffeeShopname() {
        return selectedCoffeeShopName;
    }
}
