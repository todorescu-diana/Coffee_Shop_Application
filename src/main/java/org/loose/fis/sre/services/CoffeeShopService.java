package org.loose.fis.sre.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.exceptions.CoffeeShopAlreadyExistsException;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.CoffeeShopMenuItem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.loose.fis.sre.services.FileSystemService.getPathToFile;

public class CoffeeShopService {

    private static ObjectRepository<CoffeeShop> coffeeShopsRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("coffeeShops.db").toFile())
                .openOrCreate("test", "test");

        coffeeShopsRepository = database.getRepository(CoffeeShop.class);
    }

    public static ObjectRepository<CoffeeShop> getCoffeeShopsRepository() {
        return coffeeShopsRepository;
    }

    public static void addCoffeeShop(String name, String owner) throws InvalidIdException, CoffeeShopAlreadyExistsException {
        checkCoffeeShopDoesNotAlreadyExist(name);
        coffeeShopsRepository.insert(new CoffeeShop(name, owner));
    }

    public static void modifyCoffeeShop(CoffeeShop shop) {
        coffeeShopsRepository.update(shop);
    }
    private static void checkCoffeeShopDoesNotAlreadyExist(String name) throws CoffeeShopAlreadyExistsException {
        for (CoffeeShop shop : coffeeShopsRepository.find()) {
            if (Objects.equals(name, shop.getName()))
                throw new CoffeeShopAlreadyExistsException(name);
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
