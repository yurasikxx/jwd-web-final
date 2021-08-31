package com.epam.jwd.command.page;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;

/**
 * A {@code ShowSuccessPageCommand} class implements {@code Command}
 * interface and execute command that showing success page.
 *
 * @see Command
 */
public class ShowSuccessPageCommand implements Command {

    private static volatile ShowSuccessPageCommand instance;

    private final BaseCommandResponse successCommandResponse;

    private ShowSuccessPageCommand() {
        this.successCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, false);
    }

    public static ShowSuccessPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowSuccessPageCommand.class) {
                if (instance == null) {
                    instance = new ShowSuccessPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return successCommandResponse;
    }

}