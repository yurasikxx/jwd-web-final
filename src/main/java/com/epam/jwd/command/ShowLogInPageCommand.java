package com.epam.jwd.command;

public class ShowLogInPageCommand implements Command {

    private static final String LOGIN_JSP_PATH = "/WEB-INF/jsp/login.jsp";
    private static volatile ShowLogInPageCommand instance;
    private final BaseCommandResponse loginPageResponse = new CommandResponse(LOGIN_JSP_PATH, false);

    public static ShowLogInPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowLogInPageCommand.class) {
                if (instance == null) {
                    instance = new ShowLogInPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return loginPageResponse;
    }

}
