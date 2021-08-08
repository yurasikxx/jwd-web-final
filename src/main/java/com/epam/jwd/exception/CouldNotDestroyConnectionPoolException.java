package com.epam.jwd.exception;

/**
 * A {@code CouldNotDestroyConnectionPoolException} class catch
 * connection pool destroying problems.
 */
public class CouldNotDestroyConnectionPoolException extends Exception {

    public CouldNotDestroyConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }

}