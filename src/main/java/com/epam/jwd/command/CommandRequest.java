package com.epam.jwd.command;

import javax.servlet.http.HttpServletRequest;

public class CommandRequest implements BaseCommandRequest {

    private final HttpServletRequest request;

    public CommandRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

}
