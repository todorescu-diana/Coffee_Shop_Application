package org.loose.fis.sre.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.loose.fis.sre.services.CoffeeShopService;
import org.loose.fis.sre.services.FileSystemService;
import org.loose.fis.sre.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerNewClientTest {
    public static final String USERNAMETHATDOESNTEXISTYET = "clientThatDoesntExistYet";
    public static final String PASSWORD = "password";
    public static final String ROLECLIENT = "Client";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        CoffeeShopService.initDatabase();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Test
    void testNewClientLogin(FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAMETHATDOESNTEXISTYET);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#loginMessage").queryText()).hasText("Invalid credentials.");
        Assertions.assertThat(UserService.getAllUsers()).size().isEqualTo(0);

        robot.clickOn("#goToSignUpButton");
        robot.clickOn("#usernameField");
        robot.write(USERNAMETHATDOESNTEXISTYET);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role").clickOn(ROLECLIENT);
        robot.clickOn("#registerButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");

        robot.clickOn("#goToLogin");
        assertThat(UserService.getAllUsers()).size().isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAMETHATDOESNTEXISTYET);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#coffeeShopListClientText").queryText()).hasText("Coffee Shop List");
    }
}
