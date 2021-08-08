package com.epam.jwd.command.management;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.BETSLIP_JSP_PATH;

/**
 * A {@code ShowBetslipManagementPageCommand} class implements {@code Command}
 * interface and execute command that showing betslip management page.
 *
 * @see Command
 */
public class ShowBetslipManagementPageCommand implements Command {

    private static volatile ShowBetslipManagementPageCommand instance;

    private final BaseCommandResponse betslipCommandResponse;

    private ShowBetslipManagementPageCommand() {
        this.betslipCommandResponse = new CommandResponse(BETSLIP_JSP_PATH, false);
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