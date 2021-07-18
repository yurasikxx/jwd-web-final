package com.epam.jwd.exception;

public class CouldNotDestroyConnectionPoolException extends Exception {

    public CouldNotDestroyConnectionPoolException(String message) {
        super(message);
    }

    public CouldNotDestroyConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }

}
