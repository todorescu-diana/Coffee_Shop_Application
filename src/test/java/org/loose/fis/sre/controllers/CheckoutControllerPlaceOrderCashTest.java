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
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.services.CoffeeShopService;
import org.loose.fis.sre.services.FileSystemService;
import org.loose.fis.sre.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class CheckoutControllerPlaceOrderCashTest {
    public static final String USERNAME = "username";
    public static final String MANAGERUSERNAME = "managerusername";
    public static final String PASSWORD = "password";
    public static final String ROLECLIENT = "Client";
    public static final String ROLEMANAGER = "Client";

    public static final String COFFEESHOPNAME = "coffee shop";

    public static final String COFFEENAME1 = "coffee name 1";
    public static final String DESCRIPTION1 = "description1";
    public static final String DRINKVOLUME1 = "250";
    public static final String STRINGPRICE1 = "100.50";
    public static final float FLOATPRICE1 = 100.50F;

    public static final String COFFEENAME2 = "coffee name 2";
    public static final String DESCRIPTION2 = "description2";
    public static final String DRINKVOLUME2 = "100";
    public static final String STRINGPRICE2 = "9";
    public static final float FLOATPRICE2 = 9;

    public static final String ADDRESS = "address";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        CoffeeShopService.initDatabase();

        UserService.addUser(USERNAME, PASSWORD, ROLECLIENT);
        UserService.addUser(MANAGERUSERNAME, PASSWORD, ROLEMANAGER);

        CoffeeShopService.addCoffeeShop(COFFEESHOPNAME, MANAGERUSERNAME);

        CoffeeShop currentCoffeeShop = null;
        for(CoffeeShop coffeeShop : CoffeeShopService.getCoffeeShopsRepository().find()) {
            currentCoffeeShop = coffeeShop;
            break;
        }
        CoffeeShopMenuItem[] menuItems = new CoffeeShopMenuItem[2];
        menuItems[0] = new CoffeeShopMenuItem(COFFEENAME1, DESCRIPTION1, DRINKVOLUME1, FLOATPRICE1);
        menuItems[1] = new CoffeeShopMenuItem(COFFEENAME2, DESCRIPTION2, DRINKVOLUME2, FLOATPRICE2);
        currentCoffeeShop.setMenuItems(menuItems, 2);
        currentCoffeeShop.setMenuItemsNumber(2);

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
    void testClientPlaceOrderCash(FxRobot robot) {
        Assertions.assertThat(UserService.getAllUsers()).size().isEqualTo(2);
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#shopNameField0").queryText()).hasText(COFFEESHOPNAME);

        robot.clickOn("#goToMenuButtonId0");

        assertThat(robot.lookup("#coffeeNameField0").queryText()).hasText(COFFEENAME1);
        assertThat(robot.lookup("#coffeeNameField1").queryText()).hasText(COFFEENAME2);

        robot.clickOn("#incrementButton0");
        robot.clickOn("#incrementButton1");

        robot.clickOn("#goToCheckoutButton");

        assertThat(robot.lookup("#checkoutText").queryText()).hasText("Checkout");

        robot.clickOn("#payWith").clickOn("Cash");

        robot.clickOn("#placeOrderButton");
        assertThat(robot.lookup("#alertMessage").queryText()).hasText("Address field is required.");

        robot.clickOn("#addressField");
        robot.write(ADDRESS);

        robot.clickOn("#placeOrderButton");
        assertThat(robot.lookup("#orderPlacedText").queryText()).hasText("Your order was succesfully placed!");
    }
}
