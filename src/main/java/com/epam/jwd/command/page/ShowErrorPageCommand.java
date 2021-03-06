package com.epam.jwd.command.page;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

/**
 * A {@code ShowErrorPageCommand} class implements {@code Command}
 * interface and execute command that showing error page.
 *
 * @see Command
 */
public class ShowErrorPageCommand implements Command {

    private static final String ERROR_JSP_PATH = "/jsp/error.jsp";

    private static volatile ShowErrorPageCommand instance;

    private final BaseCommandResponse errorCommandResponse;

    private ShowErrorPageCommand() {
        this.errorCommandResponse = new CommandResponse(ERROR_JSP_PATH, false);
    }

    public static ShowErrorPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowErrorPageCommand.class) {
                if (instance == null) {
                    instance = new ShowErrorPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return errorCommandResponse;
    }

}