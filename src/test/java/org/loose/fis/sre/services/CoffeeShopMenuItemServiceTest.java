package org.loose.fis.sre.services;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.loose.fis.sre.controllers.CoffeeShopMenuController;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoffeeShopMenuItemServiceTest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ROLEMANAGER = "Coffee Shop Manager";
    public static final String COFFEESHOPNAME = "Coffee Shop Name";

    public static final String MENUITEMNAME = "menu item name";
    public static final String NEWMENUITEMNAME = "new menu item name";
    public static final String MENUITEMDESCRIPTION = "menu item description";
    public static final String NEWMENUITEMDESCRIPTION = "new menu item description";
    public static final String MENUITEMDRINKVOLUME = "menu item drink volume";
    public static final String NEWMENUITEMDRINKVOLUME = "new menu item drink volume";
    public static final Double MENUITEMPRICEDOUBLE = 10.50;
    public static final Double NEWMENUITEMPRICEDOUBLE = 4.63;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before Class");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After Class");
    }

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        CoffeeShopService.initDatabase();

        UserService.addUser(USERNAME, PASSWORD, ROLEMANAGER);
        CoffeeShopService.addCoffeeShop(COFFEESHOPNAME, USERNAME);
        CoffeeShopMenuController.setCurrentCoffeeShop(CoffeeShopService.getAllCoffeeShops().get(0));
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each");
    }


    @Test
    @DisplayName("Database is initialized, and there is a user and a coffee shop with empty menu items")
    void testDatabasesAreInitializedAndAUserAndACoffeeShopWithEmptyMenuItemsIsPersisted() {
        assertThat(UserService.getAllUsers()).isNotNull();
        assertThat(UserService.getAllUsers()).size().isEqualTo(1);

        assertThat(CoffeeShopService.getAllCoffeeShops()).isNotNull();
        assertThat(CoffeeShopService.getAllCoffeeShops()).size().isEqualTo(1);
        CoffeeShop coffeeShop = CoffeeShopService.getAllCoffeeShops().get(0);

        assertThat(coffeeShop.getName()).isEqualTo(COFFEESHOPNAME);
        assertThat(coffeeShop.getOwner()).isEqualTo(USERNAME);
        assertThat(coffeeShop.getMenuItems()).isEqualTo(null);
        assertThat(coffeeShop.getMenuItemsNumber()).isEqualTo(0);
        CoffeeShopMenuController.setCurrentCoffeeShop(CoffeeShopService.getAllCoffeeShops().get(0));
        assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getName()).isEqualTo(COFFEESHOPNAME);
        assertThat(CoffeeShopMenuController.getCurrentCoffeeShop().getOwner()).isEqualTo(USERNAME);
    }

    @Test()
    @DisplayName("User client is successfully persisted to Database")
    void testMenuItemIsAddedToCoffeeShopMenuItemsAndCanBeEdited() throws MenuItemAlreadyExistsException {
        CoffeeShopMenuItemService.addMenuItem(MENUITEMNAME, MENUITEMDESCRIPTION, MENUITEMDRINKVOLUME, MENUITEMPRICEDOUBLE);

        CoffeeShop coffeeShop = CoffeeShopService.getAllCoffeeShops().get(0);
        assertThat(coffeeShop.getMenuItems()).isNotNull();
        assertThat(coffeeShop.getMenuItemsNumber()).isEqualTo(1);
        assertThat(coffeeShop.getMenuItems()[0].getName()).isEqualTo(MENUITEMNAME);
        assertThat(coffeeShop.getMenuItems()[0].getDescription()).isEqualTo(MENUITEMDESCRIPTION);
        assertThat(coffeeShop.getMenuItems()[0].getDrinkVolume()).isEqualTo(MENUITEMDRINKVOLUME);
        assertThat(coffeeShop.getMenuItems()[0].getPrice()).isEqualTo(MENUITEMPRICEDOUBLE);

        CoffeeShopMenuItemService.modifyMenuItem(MENUITEMNAME, NEWMENUITEMNAME, MENUITEMDESCRIPTION, MENUITEMDRINKVOLUME, MENUITEMPRICEDOUBLE);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItemsNumber()).isEqualTo(1);
        assertThat(CoffeeShopService.getAllCoffeeShops()).size().isEqualTo(1);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getName()).isEqualTo(NEWMENUITEMNAME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDescription()).isEqualTo(MENUITEMDESCRIPTION);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDrinkVolume()).isEqualTo(MENUITEMDRINKVOLUME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getPrice()).isEqualTo(MENUITEMPRICEDOUBLE);

        CoffeeShopMenuItemService.modifyMenuItem(NEWMENUITEMNAME, MENUITEMNAME, NEWMENUITEMDESCRIPTION, MENUITEMDRINKVOLUME, MENUITEMPRICEDOUBLE);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItemsNumber()).isEqualTo(1);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getName()).isEqualTo(MENUITEMNAME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDescription()).isEqualTo(NEWMENUITEMDESCRIPTION);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDrinkVolume()).isEqualTo(MENUITEMDRINKVOLUME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getPrice()).isEqualTo(MENUITEMPRICEDOUBLE);

        CoffeeShopMenuItemService.modifyMenuItem(MENUITEMNAME, MENUITEMNAME, MENUITEMDESCRIPTION, NEWMENUITEMDRINKVOLUME, MENUITEMPRICEDOUBLE);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItemsNumber()).isEqualTo(1);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getName()).isEqualTo(MENUITEMNAME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDescription()).isEqualTo(MENUITEMDESCRIPTION);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDrinkVolume()).isEqualTo(NEWMENUITEMDRINKVOLUME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getPrice()).isEqualTo(MENUITEMPRICEDOUBLE);

        CoffeeShopMenuItemService.modifyMenuItem(MENUITEMNAME, MENUITEMNAME, MENUITEMDESCRIPTION, MENUITEMDRINKVOLUME, NEWMENUITEMPRICEDOUBLE);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItemsNumber()).isEqualTo(1);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getName()).isEqualTo(MENUITEMNAME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDescription()).isEqualTo(MENUITEMDESCRIPTION);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getDrinkVolume()).isEqualTo(MENUITEMDRINKVOLUME);
        assertThat(CoffeeShopService.getAllCoffeeShops().get(0).getMenuItems()[0].getPrice()).isEqualTo(NEWMENUITEMPRICEDOUBLE);
    }
}
