package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.PASSWORD_CHANGING_ATTRIBUTE_NAME;

/**
 * A {@code ShowPasswordChangingCommand} class implements {@code Command}
 * interface and execute command that showing password changing page.
 *
 * @see Command
 */
public class ShowPasswordChangingCommand implements Command {

    private static final String PASSWORD_CHANGING_MESSAGE_KEY = "password.changing";

    private static volatile ShowPasswordChangingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BaseCommandResponse passwordChangingCommandResponse;

    private ShowPasswordChangingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.passwordChangingCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowPasswordChangingCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPasswordChangingCommand.class) {
                if (instance == null) {
                    instance = new ShowPasswordChangingCommand();
                }
            }
        }

        return instance;
    }

    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(PASSWORD_CHANGING_ATTRIBUTE_NAME, messageManager.getString(PASSWORD_CHANGING_MESSAGE_KEY));
        return passwordChangingCommandResponse;
    }

}