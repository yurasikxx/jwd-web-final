package com.epam.jwd.exception;

/**
 * A {@code DaoException} class catch DAO problems.
 */
public class DaoException extends Exception {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

}