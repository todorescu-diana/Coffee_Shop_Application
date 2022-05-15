package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OrderPlacedScreenController {

    @FXML
    Button homeButton;
    public void handleGoToHomeScreen(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) homeButton.getScene().getWindow();
        currentStage.close();

        Parent coffeeShopMenuClient = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeShopMenuClient.fxml")));
        Scene newScene = new Scene(coffeeShopMenuClient);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }
}
