
package org.loose.fis.sre.services;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.loose.fis.sre.exceptions.CardAlreadyExistsException;
import org.loose.fis.sre.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.sre.model.Card;
import org.loose.fis.sre.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardServiceTest {

    public static final String CARDNUMBER1 = "98765";
    public static final double CARDNUMBER1BALANCE = 100.5;
    public static final String CARDNUMBER2 = "100010";
    public static final double CARDNUMBER2BALANCE = 20;
    public static final String CARDNUMBER3 = "236790";
    public static final double CARDNUMBER3BALANCE = 500.70;
    public static final double CARDNUMBER3BALANCENEW = 6.1;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before Class");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After Class");
    }

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        CardService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each");
    }


    @Test
    @DisplayName("Database is initialized, and there are no cards")
    void testDatabaseIsInitializedAndNoCardIsPersisted() {
        assertThat(CardService.getAllCards()).isNotNull();
        assertThat(CardService.getAllCards()).size().isEqualTo(0);
    }

    @Test()
    @DisplayName("User client is successfully persisted to Database")
    void testCardIsAddedToDatabase() throws CardAlreadyExistsException {
        CardAlreadyExistsException exception = assertThrows(
                CardAlreadyExistsException.class,
                () -> {
                    CardService.addCard(CARDNUMBER1, CARDNUMBER1BALANCE);
                    CardService.addCard(CARDNUMBER1, CARDNUMBER1BALANCE);
                    CardService.addCard(CARDNUMBER1, CARDNUMBER1BALANCE);
                },
                "Exception wasn't thrown"
        );

        assertTrue(exception.getMessage().contains(String.format("A card with the card number %s already exists.", CARDNUMBER1)));
        assertThat(CardService.getAllCards()).isNotEmpty();
        assertThat(CardService.getAllCards()).size().isEqualTo(1);
        Card card = CardService.getAllCards().get(0);
        assertThat(card).isNotNull();
        assertThat(card.getCardNumber()).isEqualTo(CARDNUMBER1);
        assertThat(card.getBalance()).isEqualTo(CARDNUMBER1BALANCE);
    }

    @Test
    @DisplayName("Same card can not be added twice")
    void testCardCanNotBeAddedTwice() {
        assertThrows(CardAlreadyExistsException.class, () -> {
            CardService.addCard(CARDNUMBER2, CARDNUMBER2BALANCE);
            CardService.addCard(CARDNUMBER2, CARDNUMBER2BALANCE);
        });
    }

    @Test
    @DisplayName("Card in database can be modified and is persisted")
    void testCardCanBeModified() throws CardAlreadyExistsException {
        CardService.addCard(CARDNUMBER3, CARDNUMBER3BALANCE);
        Card card = CardService.getAllCards().get(0);
        card.setBalance(CARDNUMBER3BALANCENEW);
        CardService.modifyCard(card);
        assertThat(card.getCardNumber()).isEqualTo(CARDNUMBER3);
        assertThat(card.getBalance()).isEqualTo(CARDNUMBER3BALANCENEW);
        assertThat(CardService.getAllCards().get(0).getBalance()).isEqualTo(CARDNUMBER3BALANCENEW);
    }
}