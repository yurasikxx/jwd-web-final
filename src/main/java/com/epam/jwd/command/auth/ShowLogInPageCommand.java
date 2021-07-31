package com.epam.jwd.command.auth;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.LOGIN_JSP_PATH;

public class ShowLogInPageCommand implements Command {

    private static volatile ShowLogInPageCommand instance;

    private final BaseCommandResponse loginPageResponse;

    private ShowLogInPageCommand() {
        this.loginPageResponse = new CommandResponse(LOGIN_JSP_PATH, false);
    }

    public static ShowLogInPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowLogInPageCommand.class) {
                if (instance == null) {
                    instance = new ShowLogInPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return loginPageResponse;
    }

}