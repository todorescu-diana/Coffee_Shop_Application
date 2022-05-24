package org.loose.fis.sre.exceptions;

public class CardAlreadyExistsException extends Exception{
    public CardAlreadyExistsException(String cardNumber) {
        super(String.format("A card with the card number %s already exists.", cardNumber));
    }
}
