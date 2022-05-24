package org.loose.fis.sre.model;

import org.dizitart.no2.objects.Id;

public class Card {
    @Id
    private String cardNumber;
    private double balance = 0;
    public Card(String cardNumber, double balance) {
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

    public double getBalance() { return balance; }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}