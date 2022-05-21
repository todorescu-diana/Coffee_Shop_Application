package org.loose.fis.sre.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.loose.fis.sre.services.FileSystemService;
import org.loose.fis.sre.services.CoffeeShopService;
import org.loose.fis.sre.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import java.util.Objects;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class RegistrationControllerCoffeeShopManagerTest {

    public static final String USERNAME = "testMadeUser";
    public static final String PASSWORD = "testMadePassword";
    public static final String ROLEMANAGER = "Coffee Shop Manager";
    public static final String COFFEESHOPNAME = "testMadeCoffeeShopName";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        CoffeeShopService.initDatabase();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("register.fxml")));
        primaryStage.setTitle("Registration Example");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Test
    void testCoffeeShopManagerRegistration(FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role").clickOn(ROLEMANAGER);
        robot.clickOn("#coffeeShopNameField");
        robot.write(COFFEESHOPNAME);

        robot.clickOn("#registerButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");
        assertThat(CoffeeShopService.getAllCoffeeShops()).size().isEqualTo(1);

        robot.clickOn("#registerButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText(
                String.format("An account with the username %s already exists!", USERNAME)
        );

        robot.clickOn("#usernameField");
        robot.write("new");
        robot.clickOn("#registerButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText(
                String.format("A coffee shop with the name %s already exists.", COFFEESHOPNAME)
        );

        robot.clickOn("#coffeeShopNameField");
        robot.write("new");
        robot.clickOn("#registerButton");
        assertThat(CoffeeShopService.getAllCoffeeShops()).size().isEqualTo(2);
    }
}