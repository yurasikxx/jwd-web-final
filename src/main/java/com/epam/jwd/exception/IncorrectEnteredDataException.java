package com.epam.jwd.exception;

public class IncorrectEnteredDataException extends Exception {

    public IncorrectEnteredDataException(String message) {
        super(message);
    }

    public IncorrectEnteredDataException(String message, Throwable cause) {
        super(message, cause);
    }

}