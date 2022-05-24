package org.loose.fis.sre.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.exceptions.CardAlreadyExistsException;
import org.loose.fis.sre.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.sre.model.Card;
import org.loose.fis.sre.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import static org.loose.fis.sre.services.FileSystemService.getPathToFile;

public class CardService {

    private static ObjectRepository<Card> cardRepository;

    public static void initDatabase() throws CardAlreadyExistsException {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("cardServiceDatabase.db").toFile())
                .openOrCreate("test", "test");

        cardRepository = database.getRepository(Card.class);
    }

    public static ObjectRepository<Card> getCardRepository() {
        return cardRepository;
    }

    public static List<Card> getAllCards() {
        return cardRepository.find().toList();
    }

    public static void addCard(String cardNumber, double balance) throws InvalidIdException, CardAlreadyExistsException {
        checkCardDoesNotAlreadyExist(cardNumber);
        cardRepository.insert(new Card(cardNumber, balance));
    }

    public static void modifyCard(Card card) {
        cardRepository.update(card);
    }

    private static void checkCardDoesNotAlreadyExist(String cardNumber) throws CardAlreadyExistsException {
        for (Card card : cardRepository.find()) {
            if (Objects.equals(cardNumber, card.getCardNumber()))
                throw new CardAlreadyExistsException(cardNumber);
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