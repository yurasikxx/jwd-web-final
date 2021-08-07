package com.epam.jwd.command;

/**
 * A {@code BaseCommandResponse} interface manages JSP file paths and its redirect.
 *
 * @see CommandResponse
 */
public interface BaseCommandResponse {

    /**
     * Gets JSP file by given path.
     *
     * @return JSP file.
     */
    String getPath();

    /**
     * Indicate if is redirect.
     *
     * @return {@code true} if is redirect; {@code false} otherwise.
     */
    boolean isRedirect();

}