package org.loose.fis.sre.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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
public class LoginControllerClientTest {
    public static final String USERNAMETHATALREADYEXISTS = "clientThatAlreadyExists";
    public static final String PASSWORD = "password";
    public static final String WRONGPASSWORD = "wrongpassword";
    public static final String ROLECLIENT = "Client";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        CoffeeShopService.initDatabase();

        UserService.addUser(USERNAMETHATALREADYEXISTS, PASSWORD, ROLECLIENT);
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Test
    void testClientLogin(FxRobot robot) {
        Assertions.assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        robot.clickOn("#usernameField");
        robot.write(USERNAMETHATALREADYEXISTS);
        robot.clickOn("#passwordField");
        robot.write(WRONGPASSWORD);

        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#loginMessage").queryText()).hasText("Invalid credentials.");

        robot.clickOn("#passwordField");
        robot.press(KeyCode.CONTROL);
        robot.press(KeyCode.A);
        robot.write(PASSWORD);
        robot.release(KeyCode.CONTROL);
        robot.release(KeyCode.A);

        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#coffeeShopListClientText").queryText()).hasText("Coffee Shop List");
    }
}
