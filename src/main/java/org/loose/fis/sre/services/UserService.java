package org.loose.fis.sre.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.controllers.CoffeeShopMenuController;
import org.loose.fis.sre.exceptions.InvalidCredentialsException;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.model.Order;
import org.loose.fis.sre.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import static org.loose.fis.sre.services.FileSystemService.getPathToFile;

public class UserService {

    private static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("registration-example.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }

    public static List<User> getAllUsers() {
        return userRepository.find().toList();
    }

    public static ObjectRepository<User> getUserRepository() {
        return userRepository;
    }

    public static void addUser(String username, String password, String role) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        userRepository.insert(new User(username, encodePassword(username, password), role));
    }

    public static void removeUser(String username) {
        User userToBeRemoved = null;
        for(User user : userRepository.find()) {
            if(Objects.equals(user.getUsername(), username)) userToBeRemoved = user;
        }
        if(userToBeRemoved != null) userRepository.remove(userToBeRemoved);
    }

    public static void modifyUser(User user) {
        userRepository.update(user);
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
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

    public static void checkCredentials(String username, String password) throws InvalidCredentialsException
    {
        for(  User user : userRepository.find() ) {
            if(Objects.equals(username, user.getUsername()) && Objects.equals(encodePassword(username, password), user.getPassword()))
                return;
        }
       throw new InvalidCredentialsException();
    }

}
