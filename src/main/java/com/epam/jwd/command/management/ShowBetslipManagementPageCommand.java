package com.epam.jwd.command.management;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

public class ShowBetslipManagementPageCommand implements Command {

    private static final String BETSLIP_JSP_PATH = "/jsp/betslip.jsp";

    private static volatile ShowBetslipManagementPageCommand instance;

    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(BETSLIP_JSP_PATH, false);

    private ShowBetslipManagementPageCommand() {
    }

    public static ShowBetslipManagementPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipManagementPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipManagementPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return betslipCommandResponse;
    }

}