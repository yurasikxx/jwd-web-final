package com.epam.jwd.command;

import static com.epam.jwd.command.ShowBetslipAddingPageCommand.BET_TYPE_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.COEFFICIENT_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.ENTER_BET_TYPE_ID_MSG;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.ENTER_COEFFICIENT_MSG;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.ENTER_COMPETITION_ID_MSG;
import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.CHANGING_JSP_PATH;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.ID_ATTRIBUTE_NAME;

public class ShowBetslipChangingPageCommand implements Command {

    protected static final String ENTER_ID_OF_CHANGEABLE_BETSLIP_MSG = "Enter ID of changeable betslip: ";

    private static final String BETSLIP_CHANGING_OPERATION_MSG = "Betslip changing operation";

    private static volatile ShowBetslipChangingPageCommand instance;

    private final BaseCommandResponse betslipCommandResponse;

    private ShowBetslipChangingPageCommand() {
        this.betslipCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowBetslipChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_CHANGING_OPERATION_MSG);
        request.setAttribute(ID_ATTRIBUTE_NAME, ENTER_ID_OF_CHANGEABLE_BETSLIP_MSG);
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, ENTER_COMPETITION_ID_MSG);
        request.setAttribute(BET_TYPE_ATTRIBUTE_NAME, ENTER_BET_TYPE_ID_MSG);
        request.setAttribute(COEFFICIENT_ATTRIBUTE_NAME, ENTER_COEFFICIENT_MSG);

        return betslipCommandResponse;
    }

}