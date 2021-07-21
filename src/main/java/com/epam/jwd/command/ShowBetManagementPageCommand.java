package com.epam.jwd.command;

public class ShowBetManagementPageCommand implements Command {

    private static final String BET_JSP_PATH = "/WEB-INF/jsp/bet.jsp";

    private static volatile ShowBetManagementPageCommand instance;

    private final BaseCommandResponse betCommandResponse = new CommandResponse(BET_JSP_PATH, false);

    private ShowBetManagementPageCommand() {
    }

    public static ShowBetManagementPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetManagementPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetManagementPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return betCommandResponse;
    }

}