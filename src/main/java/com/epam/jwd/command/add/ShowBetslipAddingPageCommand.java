package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;

public class ShowBetslipAddingPageCommand implements Command {

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

        return betslipCommandResponse;
    }

}