package com.epam.jwd.command;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * A {@code BaseCommandRequest} interface manages application command requests.
 *
 * @see CommandRequest
 */
public interface BaseCommandRequest {

    /**
     * Creates session and returns it.
     *
     * @return a new session;
     */
    HttpSession createSession();

    /**
     * Gets current session and returns it.
     *
     * @return a current session.
     */
    Optional<HttpSession> getCurrentSession();

    /**
     * Invalidates current session.
     */
    void invalidateCurrentSession();

    /**
     * Gets parameter by accepting name and returns it.
     *
     * @param name a given parameter name.
     * @return a parameter string.
     */
    String getParameter(String name);

    /**
     * Sets attribute by accepting name and value.
     *
     * @param name  an attribute name.
     * @param value an attribute value.
     */
    void setAttribute(String name, Object value);

}