package com.epam.jwd.command;

import static com.epam.jwd.command.ShowBetAddingPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetAddingPageCommand.BET_TOTAL_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_BETSLIP_ID_MSG;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_BET_TOTAL_MSG;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_PERSON_ID_MSG;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.CHANGING_JSP_PATH;

public class ShowBetChangingPageCommand implements Command {

    protected static final String ENTER_ID_OF_CHANGEABLE_BET_MSG = "Enter ID of changeable bet: ";
    protected static final String BET_ID_ATTRIBUTE_NAME = "id";
    protected static final String BET_CHANGE_ATTRIBUTE_NAME = "bet";
    protected static final String PERSON_ATTRIBUTE_NAME = "betPerson";

    private static final String BET_CHANGING_OPERATION_MSG = "Bet changing operation";

    private static volatile ShowBetChangingPageCommand instance;

    private final BaseCommandResponse betCommandResponse;

    private ShowBetChangingPageCommand() {
        this.betCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowBetChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_CHANGE_ATTRIBUTE_NAME, BET_CHANGING_OPERATION_MSG);
        request.setAttribute(BET_ID_ATTRIBUTE_NAME, ENTER_ID_OF_CHANGEABLE_BET_MSG);
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, ENTER_BETSLIP_ID_MSG);
        request.setAttribute(BET_TOTAL_ATTRIBUTE_NAME, ENTER_BET_TOTAL_MSG);
        request.setAttribute(PERSON_ATTRIBUTE_NAME, ENTER_PERSON_ID_MSG);

        return betCommandResponse;
    }

}