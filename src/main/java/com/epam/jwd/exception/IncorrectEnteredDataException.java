package com.epam.jwd.exception;

/**
 * A {@code IncorrectEnteredDataException} class catch incorrect entered data.
 */
public class IncorrectEnteredDataException extends Exception {

    public IncorrectEnteredDataException(String message) {
        super(message);
    }

    public IncorrectEnteredDataException(String message, Throwable cause) {
        super(message, cause);
    }

}