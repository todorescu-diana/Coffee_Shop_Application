package org.loose.fis.sre.services;

import javafx.scene.control.ChoiceBox;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.controllers.CoffeeShopMenuController;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.loose.fis.sre.services.FileSystemService.getPathToFile;

public class CoffeeShopMenuItemService {

    public static void addMenuItem(String name, String description, String drinkVolume, double price) throws InvalidIdException, MenuItemAlreadyExistsException {
        checkMenuItemDoesNotAlreadyExist(name);
        CoffeeShopMenuItem[] newMenuItems = new CoffeeShopMenuItem[CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber() + 1];
        int count = 0;

        if(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber() > 0) {
            for(CoffeeShopMenuItem item : CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItems()) {
                newMenuItems[count++] = item;
            }
        }

        newMenuItems[count] = new CoffeeShopMenuItem(name, description, drinkVolume, price);

        CoffeeShopMenuController.getCurrentCoffeeShop().setMenuItemsNumber( CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber() + 1 );

        CoffeeShopMenuController.getCurrentCoffeeShop().setMenuItems(newMenuItems, CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber());

        CoffeeShopService.modifyCoffeeShop(CoffeeShopMenuController.getCurrentCoffeeShop());
//        for(CoffeeShopMenuItem item : CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItems()) {
//            System.out.println(item.getPrice());
//        }
    }

    public static void removeMenuItem(String name, String description, String drinkVolume) {
        CoffeeShopMenuItem[] newMenuItems = new CoffeeShopMenuItem[CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber() - 1];
        int count = 0;

        for(CoffeeShopMenuItem item : CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItems()) {
            if(!Objects.equals(item.getName(), name)) {
                newMenuItems[count++] = item;
            }
        }

        CoffeeShopMenuController.getCurrentCoffeeShop().setMenuItemsNumber( CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber() - 1);

        CoffeeShopMenuController.getCurrentCoffeeShop().setMenuItems(newMenuItems, CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber());

        CoffeeShopService.modifyCoffeeShop(CoffeeShopMenuController.getCurrentCoffeeShop());
    }

    public static void modifyMenuItem(String oldName, String name, String description, String drinkVolume, double price) throws MenuItemAlreadyExistsException {
        if(!oldName.equals(name)) {
            removeMenuItem(oldName, description, drinkVolume);
            addMenuItem(name, description, drinkVolume, price);
        }
        else {
            CoffeeShopMenuItem[] newMenuItems = new CoffeeShopMenuItem[CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber()];
            int count = 0;

            for(CoffeeShopMenuItem item : CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItems()) {
                if(Objects.equals(item.getName(), name)) {
                    item.setName(name);
                    item.setDescription(description);
                    item.setDrinkVolume(drinkVolume);
                    item.setPrice(price);
                }
                newMenuItems[count++] = item;
            }

            CoffeeShopMenuController.getCurrentCoffeeShop().setMenuItems(newMenuItems, CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber());

            CoffeeShopService.modifyCoffeeShop(CoffeeShopMenuController.getCurrentCoffeeShop());
        }
    }

    private static void checkMenuItemDoesNotAlreadyExist(String name) throws MenuItemAlreadyExistsException {
        if(CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItemsNumber() > 0) {
            for (CoffeeShopMenuItem item : CoffeeShopMenuController.getCurrentCoffeeShop().getMenuItems()) {
                if(item != null) {
                    if (Objects.equals(name, item.getName()))
                        throw new MenuItemAlreadyExistsException(name);
                }
            }
        }
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }

}
