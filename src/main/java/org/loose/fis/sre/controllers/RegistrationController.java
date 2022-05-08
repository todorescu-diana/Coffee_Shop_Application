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
import org.dizitart.no2.exceptions.InvalidIdException;
import org.loose.fis.sre.exceptions.CoffeeShopAlreadyExistsException;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.sre.services.CoffeeShopMenuItemService;
import org.loose.fis.sre.services.CoffeeShopService;
import org.loose.fis.sre.services.UserService;

import java.io.IOException;
import java.util.Objects;

public class RegistrationController {

    @FXML
    private Text registrationMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox role;

    @FXML VBox VBoxContainer;

    private String coffeeShopRegistrationName;

    @FXML
    public void initialize() {
        role.getItems().addAll("Client", "Coffee Shop Manager");

        role.setOnAction((event) -> {
            if (Objects.equals(role.getValue(), "Coffee Shop Manager")) {
                HBox HBoxContainer = new HBox();
                Text itemMessage = new Text("Coffee Shop Name:");
                TextField coffeeShopNameField = new TextField();
                coffeeShopNameField.textProperty().addListener((observable, oldValue, newValue) -> {
                    coffeeShopRegistrationName = newValue;
                });
                HBoxContainer.getChildren().addAll(itemMessage, coffeeShopNameField);
                VBoxContainer.getChildren().add(HBoxContainer);
            }
        });
    }

    @FXML
    public void handleRegisterAction() {
        try {
            UserService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue());
            if(Objects.equals(role.getValue(), "Coffee Shop Manager")) CoffeeShopService.addCoffeeShop(coffeeShopRegistrationName, usernameField.getText());
            registrationMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException | CoffeeShopAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        }
    }

    public void handleGoToLoginAction(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        currentStage.close();

        Parent login = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        Scene scene = new Scene(login);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(scene);
        newStage.show();
    }
}
