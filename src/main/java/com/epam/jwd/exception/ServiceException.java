package com.epam.jwd.exception;

/**
 * A {@code ServiceException} class catch service problems.
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}