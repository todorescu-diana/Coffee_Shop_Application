package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.User;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.loose.fis.sre.services.CoffeeShopService.getCoffeeShopsRepository;
import static org.loose.fis.sre.services.UserService.getUserRepository;

public class CoffeeShopListController {
    @FXML
    private VBox verticalBoxContainer;
    static String selectedCoffeeShopName;

    public void initialize () {
        ObjectRepository<CoffeeShop> coffeeShopsRepository = getCoffeeShopsRepository();

        int shopIndex = 0;
        verticalBoxContainer.setSpacing(30);
        verticalBoxContainer.setStyle("-fx-padding: 0 0 30 25");
        for(CoffeeShop shop: coffeeShopsRepository.find()) {
            createNewShopContainer(shop.getName(), shopIndex);
            shopIndex++;
        }
    }

    public void createNewShopContainer(String name, int shopIndex) {
        HBox newHBox = new HBox();

        VBox newVBoxInfo = new VBox(10);

        Text nameField = new Text(name);
        nameField.setFill(Color.MAROON);
        String nameFieldId = "shopNameField" + String.valueOf(shopIndex);
        nameField.setId(nameFieldId);

        Button goToMenuButton = new Button("Go to coffee shop's menu");
        goToMenuButton.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc;");
        String goToMenuButtonId = "goToMenuButtonId" + String.valueOf(shopIndex);
        goToMenuButton.setId(goToMenuButtonId);

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

        newVBoxInfo.getChildren().addAll(nameField, goToMenuButton);

        newHBox.getChildren().addAll(newVBoxInfo);

        verticalBoxContainer.getChildren().add(newHBox);
    }

    public static String getSelectedCoffeeShopName() {
        return selectedCoffeeShopName;
    }

    public void handleGoToCoffeeShopListPress(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) verticalBoxContainer.getScene().getWindow();
        currentStage.close();

        Parent coffeeShopList = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeShopList.fxml")));
        Scene newScene = new Scene(coffeeShopList);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }

    public void handleGoToPastOrdersPress(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) verticalBoxContainer.getScene().getWindow();

        Parent coffeeShopList = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("orderListClient.fxml")));
        Scene newScene = new Scene(coffeeShopList);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        currentStage.close();
        newStage.show();
    }
}