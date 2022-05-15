package org.loose.fis.sre.model;

public class Card {
    private String cardNumber;
    private int balance = 0;

    public Card(String cardNumber, int balance) {
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    public Card() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getBalance() { return balance; }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
