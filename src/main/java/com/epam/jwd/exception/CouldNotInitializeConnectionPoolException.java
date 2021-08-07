package com.epam.jwd.exception;

/**
 * A {@code CouldNotInitializeConnectionPoolException} class catch
 * connection pool initializing problems.
 */
public class CouldNotInitializeConnectionPoolException extends Exception {

    public CouldNotInitializeConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }

}