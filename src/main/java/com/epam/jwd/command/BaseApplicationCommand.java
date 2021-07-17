package com.epam.jwd.command;

public enum BaseApplicationCommand {
    MAIN_PAGE(ShowMainPageCommand.getInstance()),
    LOG_IN_PAGE(ShowLogInPageCommand.getInstance()),
    LOG_IN(LogInCommand.getInstance()),
    LOG_OUT(LogOutCommand.getInstance()),
    COMPETITION_PAGE(ShowCompetitionPageCommand.getInstance()),
    DEFAULT(ShowMainPageCommand.getInstance());

    private final Command command;

    BaseApplicationCommand(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    static BaseApplicationCommand of(String name) {
        for (BaseApplicationCommand command : values()) {
            if (command.name().equalsIgnoreCase(name)) {
                return command;
            }
        }

        return DEFAULT;
    }
}
