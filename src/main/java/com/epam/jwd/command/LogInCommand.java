package com.epam.jwd.command;

public class LogInCommand implements Command {

    private static volatile LogInCommand instance;

    public static LogInCommand getInstance() {
        if (instance == null) {
            synchronized (LogInCommand.class) {
                if (instance == null) {
                    instance = new LogInCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return null;
    }

}
