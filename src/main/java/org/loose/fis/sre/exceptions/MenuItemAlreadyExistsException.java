package org.loose.fis.sre.exceptions;

public class MenuItemAlreadyExistsException extends Exception {
    public MenuItemAlreadyExistsException(String name) {
        super(String.format("A menu item with the name %s already exists!", name));
    }
}
