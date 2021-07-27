package com.epam.jwd.command;

import static com.epam.jwd.command.ShowAllBetsListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;

public class ShowBetAddingPageCommand implements Command {

    protected static final String BET_TOTAL_ATTRIBUTE_NAME = "betTotal";
    protected static final String ENTER_BETSLIP_ID_MSG = "Enter betslip ID: ";
    protected static final String ENTER_BET_TOTAL_MSG = "Enter bet total: ";
    protected static final String ENTER_PERSON_ID_MSG = "Enter person ID: ";
    protected static final String BETSLIP_ATTRIBUTE_NAME = "betBetslip";

    private static final String BET_ADDING_OPERATION_MSG = "Bet adding operation";

    private static volatile ShowBetAddingPageCommand instance;

    private final BaseCommandResponse betCommandResponse;

    private ShowBetAddingPageCommand() {
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowBetAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_ATTRIBUTE_NAME, BET_ADDING_OPERATION_MSG);
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, ENTER_BETSLIP_ID_MSG);
        request.setAttribute(BET_TOTAL_ATTRIBUTE_NAME, ENTER_BET_TOTAL_MSG);

        return betCommandResponse;
    }

}