package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;

public class ShowBetslipChangingPageCommand implements Command {

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
        return betslipCommandResponse;
    }

}