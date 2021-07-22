package com.epam.jwd.command;

import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class ShowBetslipDeletingPageCommand implements Command {

    private static final String ENTER_BETSLIP_ID_MSG = "Enter betslip ID which needs to be deleted";
    private static volatile ShowBetslipDeletingPageCommand instance;

    private final BaseCommandResponse betslipDeletingCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowBetslipDeletingPageCommand() {
    }

    public static ShowBetslipDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, ENTER_BETSLIP_ID_MSG);
        return betslipDeletingCommandResponse;
    }

}