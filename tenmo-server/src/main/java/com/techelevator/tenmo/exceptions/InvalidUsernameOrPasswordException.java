package com.techelevator.tenmo.exceptions;

public class InvalidUsernameOrPasswordException extends Exception {
    public InvalidUsernameOrPasswordException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Invalid username or password";
    }
}
