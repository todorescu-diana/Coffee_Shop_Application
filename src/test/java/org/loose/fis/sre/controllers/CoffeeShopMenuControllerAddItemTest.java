package org.loose.fis.sre.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
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
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import java.util.Objects;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class CoffeeShopMenuControllerAddItemTest {
    public static final String USERNAMETHATALREADYEXISTS = "managerThatAlreadyExists";
    public static final String PASSWORD = "password";
    public static final String ROLEMANAGER = "Coffee Shop Manager";
    public static final String COFFEESHOPNAME = "Test Coffee Shop";
    public static final String COFFEENAME1 = "coffee1";
    public static final String COFFEENAME2 = "coffee2";
    public static final String DESCRIPTION = "description";
    public static final String DRINKVOLUME = "350";
    public static final String PRICE = "13";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        CoffeeShopService.initDatabase();

        UserService.addUser(USERNAMETHATALREADYEXISTS, PASSWORD, ROLEMANAGER);
        CoffeeShopService.addCoffeeShop(COFFEESHOPNAME, USERNAMETHATALREADYEXISTS);
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Test
    void testCoffeeShopMenuAddItem(FxRobot robot) {
        Assertions.assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        Assertions.assertThat(CoffeeShopService.getAllCoffeeShops()).size().isEqualTo(1);
        Assertions.assertThat(CoffeeShopService.getAllCoffeeShops()).size().isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAMETHATALREADYEXISTS);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#coffeeShopMenuManagerText").queryText()).hasText("Menu");

        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getName()).isEqualTo(COFFEESHOPNAME);
        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber()).isEqualTo(0);

        assertThat(robot.lookup("#vboxchild").queryAll().size()).isEqualTo(0);

        robot.clickOn("#addItemButton");

        robot.clickOn("#editableNameField");
        robot.write(COFFEENAME1);
        robot.clickOn("#editableDescriptionField");
        robot.write(DESCRIPTION);
        robot.clickOn("#editableDrinkVolumeField");
        robot.write(DRINKVOLUME);
        robot.clickOn("#editablePriceField");
        robot.write(PRICE);

        robot.clickOn("#addNewItemButton");

        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber()).isEqualTo(1);
        assertThat(robot.lookup("#vboxchild").queryAll().size()).isEqualTo(1);

        robot.clickOn("#addItemButton");

        robot.clickOn("#editableNameField");
        robot.write(COFFEENAME1);
        robot.clickOn("#editableDescriptionField");
        robot.write(DESCRIPTION);
        robot.clickOn("#editableDrinkVolumeField");
        robot.write(DRINKVOLUME);
        robot.clickOn("#editablePriceField");
        robot.write(PRICE);

        robot.clickOn("#addNewItemButton");
        assertThat(robot.lookup("#itemMessage").queryText()).hasText(
                String.format("A menu item with the name %s already exists!", COFFEENAME1)
        );

        robot.clickOn("#editableNameField");
        robot.press(KeyCode.CONTROL);
        robot.press(KeyCode.A);
        robot.write(COFFEENAME2);
        robot.release(KeyCode.CONTROL);
        robot.release(KeyCode.A);

        robot.clickOn("#addNewItemButton");

        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber()).isEqualTo(2);
    }
}
