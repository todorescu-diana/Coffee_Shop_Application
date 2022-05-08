package org.loose.fis.sre.exceptions;

public class CoffeeShopAlreadyExistsException extends Exception{
    public CoffeeShopAlreadyExistsException(String name) {
        super(String.format("A coffee shop with the name %s already exists.", name));
    }
}
