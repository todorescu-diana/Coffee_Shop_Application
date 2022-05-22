package org.loose.fis.sre.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.model.Card;
import org.loose.fis.sre.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import static org.loose.fis.sre.services.FileSystemService.getPathToFile;

public class CardService {

    private static ObjectRepository<Card> cardRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("cards.db").toFile())
                .openOrCreate("test", "test");

        cardRepository = database.getRepository(Card.class);

        addCard("111", 999);
        addCard("222", 10);
        addCard("333", 123);
        addCard("444", 8000);
    }

    public static ObjectRepository<Card> getCardRepository() {
        return cardRepository;
    }

    public static List<Card> getAllCards() {
        return cardRepository.find().toList();
    }

    public static void addCard(String cardNumber, double balance) throws InvalidIdException {
        if(!checkCardDoesNotAlreadyExist(cardNumber)) cardRepository.insert(new Card(cardNumber, balance));
    }

    public static void modifyCard(Card card) {
        cardRepository.update(card);
    }

    private static boolean checkCardDoesNotAlreadyExist(String cardNumber) {
        for (Card card : cardRepository.find()) {
            if (Objects.equals(cardNumber, card.getCardNumber()))
            {
                return true;
            }
        }
        return false;
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
