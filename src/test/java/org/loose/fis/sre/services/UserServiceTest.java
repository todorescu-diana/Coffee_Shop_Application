package org.loose.fis.sre.services;


import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.loose.fis.sre.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.sre.model.User;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.assertions.api.Assertions.assertThat;

public class UserServiceTest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NEWPASSWORD = "newpassword";
    public static final String ROLECLIENT = "Client";
    public static final String ROLEMANAGER = "Coffee Shop Manager";

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
        UserService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each");
    }


    @Test
    @DisplayName("Database is initialized, and there are no users")
    void testDatabaseIsInitializedAndNoUserIsPersisted() {
        assertThat(UserService.getAllUsers()).isNotNull();
        assertThat(UserService.getAllUsers()).isEmpty();
    }

    @Test()
    @DisplayName("User client is successfully persisted to Database")
    void testUserClientIsAddedToDatabase() throws UsernameAlreadyExistsException {
        UsernameAlreadyExistsException exception = assertThrows(
                UsernameAlreadyExistsException.class,
                () -> {
                    UserService.addUser(USERNAME, PASSWORD, ROLECLIENT);
                    UserService.addUser(USERNAME, NEWPASSWORD, ROLECLIENT);
                    UserService.addUser(USERNAME, PASSWORD, ROLEMANAGER);
                },
                "Exception wasn't thrown"
        );

        assertTrue(exception.getMessage().contains(String.format("An account with the username %s already exists!", USERNAME)));
        assertThat(UserService.getAllUsers()).isNotEmpty();
        assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        User user = UserService.getAllUsers().get(0);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getPassword()).isEqualTo(UserService.encodePassword(USERNAME, PASSWORD));
        assertThat(user.getRole()).isEqualTo(ROLECLIENT);
    }

    @Test
    @DisplayName("User manager is successfully persisted to Database")
    void testUserManagerIsAddedToDatabase() throws UsernameAlreadyExistsException {
        UsernameAlreadyExistsException exception = assertThrows(
                UsernameAlreadyExistsException.class,
                () -> {
                    UserService.addUser(USERNAME, PASSWORD, ROLEMANAGER);
                    UserService.addUser(USERNAME, PASSWORD, ROLECLIENT);
                    UserService.addUser(USERNAME, NEWPASSWORD, ROLEMANAGER);
                },
                "Exception wasn't thrown"
        );
        assertTrue(exception.getMessage().contains(String.format("An account with the username %s already exists!", USERNAME)));
        assertThat(UserService.getAllUsers()).isNotEmpty();
        assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        User user = UserService.getAllUsers().get(0);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getPassword()).isEqualTo(UserService.encodePassword(USERNAME, PASSWORD));
        assertThat(user.getRole()).isEqualTo(ROLEMANAGER);
    }

    @Test
    @DisplayName("User client can not be added twice")
    void testUserClientCanNotBeAddedTwice() {
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLECLIENT);
            UserService.addUser(USERNAME, PASSWORD, ROLECLIENT);
        });
    }

    @Test
    @DisplayName("User manager can not be added twice")
    void testUserManagerCanNotBeAddedTwice() {
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLEMANAGER);
            UserService.addUser(USERNAME, PASSWORD, ROLEMANAGER);
        });
    }
}
