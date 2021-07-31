package com.epam.jwd.command.auth;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.INDEX_JSP_PATH;

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
        request.invalidateCurrentSession();
        return logoutCommandResponse;
    }

}