package com.epam.jwd.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * A {@code CommandRequest} class implements
 * {@code BaseCommandRequest} interface methods.
 *
 * @see BaseCommandRequest
 */
public class CommandRequest implements BaseCommandRequest {

    private final HttpServletRequest request;

    public CommandRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public HttpSession createSession() {
        return request.getSession(true);
    }

    @Override
    public Optional<HttpSession> getCurrentSession() {
        return Optional.ofNullable(request.getSession(false));
    }

    @Override
    public void invalidateCurrentSession() {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    @Override
    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

}