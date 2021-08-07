package com.epam.jwd.exception;

/**
 * An {@code UnknownEnumAttributeException} class catch illegal enum constants.
 */
public class UnknownEnumAttributeException extends Exception {

    public UnknownEnumAttributeException(String message) {
        super(message);
    }

}