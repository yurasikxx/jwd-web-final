package com.epam.jwd.command.management;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.BET_JSP_PATH;

/**
 * A {@code ShowBetManagementPageCommand} class implements {@code Command}
 * interface and execute command that showing bet management page.
 *
 * @see Command
 */
public class ShowBetManagementPageCommand implements Command {

    private static volatile ShowBetManagementPageCommand instance;

    private final BaseCommandResponse betCommandResponse;

    private ShowBetManagementPageCommand() {
        this.betCommandResponse = new CommandResponse(BET_JSP_PATH, false);
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