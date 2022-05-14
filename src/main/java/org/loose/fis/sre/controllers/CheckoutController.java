package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Objects;

public class CheckoutController {
    @FXML
    private ChoiceBox payWith;
    @FXML
    private HBox hBox;
    @FXML
    private Button placeOrderButton;

    private String cardNumber;

    public void initialize() {
        payWith.getItems().addAll("Cash", "Card");

        payWith.setOnAction((event) -> {
            if (Objects.equals(payWith.getValue(), "Card")) {
                Text itemMessage = new Text("Card number:");
                TextField cardNumberField = new TextField();
                cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
                    cardNumber = newValue;
                });
                hBox.getChildren().addAll(itemMessage, cardNumberField);
            }
        });
    }
}
