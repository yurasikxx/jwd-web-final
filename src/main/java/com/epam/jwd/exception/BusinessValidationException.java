package com.epam.jwd.exception;

public class BusinessValidationException extends Exception {

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
