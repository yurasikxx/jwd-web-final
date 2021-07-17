package com.epam.jwd.command;

public class LogOutCommand implements Command {

    private static final String INDEX_JSP_PATH = "/index.jsp";
    private static volatile LogOutCommand instance;
    private final BaseCommandResponse logoutCommandResponse = new CommandResponse(INDEX_JSP_PATH, true);

    public static LogOutCommand getInstance() {
        if (instance == null) {
            synchronized (LogOutCommand.class) {
                if (instance == null) {
                    instance = new LogOutCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.invalidateCurrentSession();
        return logoutCommandResponse;
    }
}
