package com.epam.jwd.command.management;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

/**
 * A {@code ShowBetManagementPageCommand} class implements {@code Command}
 * interface and execute command that showing bet management page.
 *
 * @see Command
 */
public class ShowBetManagementPageCommand implements Command {

    private static final String BET_JSP_PATH = "/jsp/bet.jsp";

    private static volatile ShowBetManagementPageCommand instance;

    private final BaseCommandResponse betCommandResponse = new CommandResponse(BET_JSP_PATH, false);

    private ShowBetManagementPageCommand() {
    }

    public static ShowBetManagementPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetManagementPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetManagementPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return betCommandResponse;
    }

}