package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.exceptions.InvalidCredentialsException;
import org.loose.fis.sre.model.User;
import org.loose.fis.sre.services.UserService;

import java.io.IOException;
import java.util.Objects;

import static org.loose.fis.sre.services.UserService.getUserRepository;

public class LoginController {

    @FXML
    private Text loginMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;

    @FXML
    public void initialize() {

    }

    @FXML
    public void handleLoginAction(javafx.event.ActionEvent event) {
        try {
            UserService.checkCredentials(usernameField.getText(), passwordField.getText());

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            String username = usernameField.getText();

            currentStage.close();

            ObjectRepository<User> userRepository = getUserRepository();

            String role = "";

            for(User user : userRepository.find()) {
                if(Objects.equals(username, user.getUsername())) {
                    role = user.getRole();
                }
            }

            if(role.equals("Client")) {
                Parent coffeeShopList = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeShopList.fxml")));
                Scene newScene = new Scene(coffeeShopList);
                Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                newStage.setScene(newScene);
                newStage.show();
            }

            else if (role.equals("Coffee Shop Manager")) {
                Parent coffeeShopMenu = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeShopMenu.fxml")));
                Scene newScene = new Scene(coffeeShopMenu);
                Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                newStage.setScene(newScene);
                newStage.show();
            }
        } catch (InvalidCredentialsException | IOException e) {
            loginMessage.setText(e.getMessage());
        }
    }

    public void handleGoToSignUpAction(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        currentStage.close();

        Parent register = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("register.fxml")));
        Scene newScene = new Scene(register);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }
}
