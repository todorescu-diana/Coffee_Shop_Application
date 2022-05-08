package org.loose.fis.sre.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.model.CoffeeShopMenuItem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.loose.fis.sre.services.FileSystemService.getPathToFile;

public class CoffeeShopMenuItemService {

    private static ObjectRepository<CoffeeShopMenuItem> menuItemsRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("menuItems.db").toFile())
                .openOrCreate("test", "test");

        menuItemsRepository = database.getRepository(CoffeeShopMenuItem.class);
    }

    public static ObjectRepository<CoffeeShopMenuItem> getMenuItemsRepository() {
        return menuItemsRepository;
    }

    public static void addMenuItem(String name, String description, String drinkVolume) throws InvalidIdException, MenuItemAlreadyExistsException {
        checkMenuItemDoesNotAlreadyExist(name);
        menuItemsRepository.insert(new CoffeeShopMenuItem(name,description, drinkVolume));
    }

    public static void removeMenuItem(String name, String description, String drinkVolume) {
        menuItemsRepository.remove(new CoffeeShopMenuItem(name,description, drinkVolume));
    }

    public static void modifyMenuItem(String oldName, String name, String description, String drinkVolume) throws MenuItemAlreadyExistsException {
        if(!oldName.equals(name)) {
            removeMenuItem(oldName, description, drinkVolume);
            addMenuItem(name, description, drinkVolume);
        }
        else {
            CoffeeShopMenuItem itemToBeModified = null;
            for (CoffeeShopMenuItem item : menuItemsRepository.find()) {
                if (Objects.equals(name, item.getName()))
                    itemToBeModified = item;
            }
            itemToBeModified.setName(name);
            itemToBeModified.setDescription(description);
            itemToBeModified.setDrinkVolume(drinkVolume);
            menuItemsRepository.update(itemToBeModified);   
        }
    }

    private static void checkMenuItemDoesNotAlreadyExist(String name) throws MenuItemAlreadyExistsException {
        for (CoffeeShopMenuItem item : menuItemsRepository.find()) {
            if (Objects.equals(name, item.getName()))
                throw new MenuItemAlreadyExistsException(name);
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
