package com.epam.jwd.command;

import javax.servlet.http.HttpServletRequest;

public class ApplicationCommand {

    private static final String COMMAND_PARAMETER_NAME = "command";

    private static volatile ApplicationCommand instance;

    private ApplicationCommand() {
    }

    public static ApplicationCommand getInstance() {
        if (instance == null) {
            synchronized (ApplicationCommand.class) {
                if (instance == null) {
                    instance = new ApplicationCommand();
                }
            }
        }

        return instance;
    }

    public Command resolveCommand(HttpServletRequest request) {
        final String commandName = request.getParameter(COMMAND_PARAMETER_NAME);
        return BaseApplicationCommand.of(commandName).getCommand();
    }

}