package com.epam.jwd.exception;

/**
 * A {@code BusinessValidationException} class catch business validation problems.
 */
public class BusinessValidationException extends Exception {

    public BusinessValidationException(String message) {
        super(message);
    }

}