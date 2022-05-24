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
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.services.CoffeeShopMenuItemService;
import org.loose.fis.sre.services.CoffeeShopService;
import org.loose.fis.sre.services.FileSystemService;
import org.loose.fis.sre.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class CoffeeShopMenuControllerEditItemEmptyNameTest {
    public static final String USERNAMETHATALREADYEXISTS = "managerThatAlreadyExists";
    public static final String PASSWORD = "password";
    public static final String ROLEMANAGER = "Coffee Shop Manager";
    public static final String COFFEESHOPNAME = "Test Coffee Shop";
    public static final String NONEMPTYCOFFEENAME = "non-empty coffee name";
    public static final String DESCRIPTION = "description";
    public static final String DRINKVOLUME = "250";
    public static final String STRINGPRICE = "100";
    public static final float FLOATPRICE = 100;
    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        CoffeeShopService.initDatabase();

        UserService.addUser(USERNAMETHATALREADYEXISTS, PASSWORD, ROLEMANAGER);
        CoffeeShopService.addCoffeeShop(COFFEESHOPNAME, USERNAMETHATALREADYEXISTS);

        CoffeeShop currentCoffeeShop = null;
        for(CoffeeShop coffeeShop : CoffeeShopService.getCoffeeShopsRepository().find()) {
            currentCoffeeShop = coffeeShop;
            break;
        }
        CoffeeShopMenuItem[] menuItems = new CoffeeShopMenuItem[1];
        menuItems[0] = new CoffeeShopMenuItem(NONEMPTYCOFFEENAME,DESCRIPTION,DRINKVOLUME,FLOATPRICE);
        currentCoffeeShop.setMenuItems(menuItems, 1);
        currentCoffeeShop.setMenuItemsNumber(1);

        CoffeeShopService.modifyCoffeeShop(currentCoffeeShop);
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Test
    void testCoffeeShopMenuEditItemEmptyName(FxRobot robot) {
        Assertions.assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        Assertions.assertThat(CoffeeShopService.getAllCoffeeShops()).size().isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAMETHATALREADYEXISTS);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#coffeeShopMenuManagerText").queryText()).hasText("My Coffee Shop's Menu");

        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getName()).isEqualTo(COFFEESHOPNAME);
        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber()).isEqualTo(1);
        Assertions.assertThat(robot.lookup("#vboxchild").queryAll().size()).isEqualTo(1);

        robot.clickOn("#editItemButton");

        robot.clickOn("#editableNameField");
        robot.press(KeyCode.CONTROL);
        robot.press(KeyCode.A);
        robot.release(KeyCode.CONTROL);
        robot.release(KeyCode.A);
        robot.press(KeyCode.BACK_SPACE);

        robot.clickOn("#addNewItemButton");

        assertThat(robot.lookup("#itemMessage").queryText()).hasText("Menu item name cannot be empty.");

        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber()).isEqualTo(1);

        robot.clickOn("#editableNameField");
        robot.write(NONEMPTYCOFFEENAME);

        robot.clickOn("#addNewItemButton");
        Assertions.assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber()).isEqualTo(1);
        Assertions.assertThat(robot.lookup("#vboxchild").queryAll().size()).isEqualTo(1);

        assertThat(robot.lookup("#nameField").queryText()).hasText(NONEMPTYCOFFEENAME);
        assertThat(robot.lookup("#descriptionField").queryText()).hasText(DESCRIPTION);
        assertThat(robot.lookup("#drinkVolumeField").queryText()).hasText(DRINKVOLUME);
        assertThat(robot.lookup("#priceField").queryText()).hasText(STRINGPRICE + ".0");
    }
}
