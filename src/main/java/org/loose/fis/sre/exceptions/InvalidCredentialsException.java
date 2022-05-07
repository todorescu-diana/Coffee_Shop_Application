package org.loose.fis.sre.exceptions;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super(String.format("Invalid credentials."));
    }
}
