package com.epam.jwd.command.auth;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import javax.servlet.http.HttpSession;

import static com.epam.jwd.constant.Constant.INDEX_JSP_PATH;
import static com.epam.jwd.constant.Constant.LOCALE_PARAMETER_NAME;

/**
 * A {@code LogOutCommand} class implements {@code Command}
 * interface and execute command that logs person out.
 *
 * @see Command
 */
public class LogOutCommand implements Command {

    private static volatile LogOutCommand instance;

    private final BaseCommandResponse logoutCommandResponse;

    private LogOutCommand() {
        this.logoutCommandResponse = new CommandResponse(INDEX_JSP_PATH, true);
    }

    public static LogOutCommand getInstance() {
        if (instance == null) {
            synchronized (LogOutCommand.class) {
                if (instance == null) {
                    instance = new LogOutCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final HttpSession currentSession;
        String locale = null;

        if (request.getCurrentSession().isPresent()) {
            currentSession = request.getCurrentSession().get();
            locale = currentSession.getAttribute(LOCALE_PARAMETER_NAME).toString();
        }

        request.invalidateCurrentSession();
        request.createSession().setAttribute(LOCALE_PARAMETER_NAME, locale);
        return logoutCommandResponse;
    }

}