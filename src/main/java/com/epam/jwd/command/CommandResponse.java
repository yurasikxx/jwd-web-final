package com.epam.jwd.command;

public class CommandResponse implements BaseCommandResponse {

    private final String path;
    private final boolean redirect;

    public CommandResponse(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isRedirect() {
        return redirect;
    }

}
