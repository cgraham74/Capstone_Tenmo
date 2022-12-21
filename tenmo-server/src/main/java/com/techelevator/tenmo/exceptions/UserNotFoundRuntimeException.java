package com.techelevator.tenmo.exceptions;

public class UserNotFoundRuntimeException extends RuntimeException {
    public UserNotFoundRuntimeException(String message){
        super(message);
    }
}
