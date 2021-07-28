package com.epam.jwd.command;

import static com.epam.jwd.command.ShowAllBetsListPageCommand.BET_ATTRIBUTE_NAME;

public class ShowBetCalculatingPageCommand implements Command {

    protected static final String BET_CALCULATING_OPERATION_MSG = "Bet calculating operation";
    protected static final String BET_HISTORY_ATTRIBUTE_NAME = "betHistory";
    protected static final String CALCULATING_JSP_PATH = "/WEB-INF/jsp/calculating.jsp";
    protected static final String ENTER_BET_ID_MSG = "Enter ID of bet that need to calculate: ";

    private static volatile ShowBetCalculatingPageCommand instance;

    private final BaseCommandResponse betHistoryCommandResponse;

    private ShowBetCalculatingPageCommand() {
        this.betHistoryCommandResponse = new CommandResponse(CALCULATING_JSP_PATH, false);
    }

    public static ShowBetCalculatingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetCalculatingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetCalculatingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, BET_CALCULATING_OPERATION_MSG);
        request.setAttribute(BET_ATTRIBUTE_NAME, ENTER_BET_ID_MSG);

        return betHistoryCommandResponse;
    }

}