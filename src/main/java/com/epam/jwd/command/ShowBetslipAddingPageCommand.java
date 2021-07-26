package com.epam.jwd.command;

import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;

public class ShowBetslipAddingPageCommand implements Command {

    protected static final String COMPETITION_ATTRIBUTE_NAME = "betslipCompetition";
    protected static final String ENTER_COMPETITION_ID_MSG = "Enter competition ID: ";
    protected static final String BET_TYPE_ATTRIBUTE_NAME = "betType";
    protected static final String ENTER_BET_TYPE_ID_MSG = "Enter bet type ID: ";
    protected static final String COEFFICIENT_ATTRIBUTE_NAME = "coefficient";
    protected static final String ENTER_COEFFICIENT_MSG = "Enter coefficient: \n" +
            "(You may enter \"0\" value if you want to calculate coefficient at random)";

    private static final String BETSLIP_ADDING_OPERATION_MSG = "Betslip adding operation";

    private static volatile ShowBetslipAddingPageCommand instance;

    private final BaseCommandResponse betslipCommandResponse;

    private ShowBetslipAddingPageCommand() {
        this.betslipCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowBetslipAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_ADDING_OPERATION_MSG);
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, ENTER_COMPETITION_ID_MSG);
        request.setAttribute(BET_TYPE_ATTRIBUTE_NAME, ENTER_BET_TYPE_ID_MSG);
        request.setAttribute(COEFFICIENT_ATTRIBUTE_NAME, ENTER_COEFFICIENT_MSG);

        return betslipCommandResponse;
    }

}