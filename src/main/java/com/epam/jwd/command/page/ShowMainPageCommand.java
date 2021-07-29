package com.epam.jwd.command.page;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

public class ShowMainPageCommand implements Command {

    private static final String MAIN_JSP_PATH = "/jsp/main.jsp";
    private static final boolean REDIRECT = false;

    private static volatile ShowMainPageCommand instance;
    private final BaseCommandResponse mainPageResponse = new CommandResponse(MAIN_JSP_PATH, REDIRECT);

    private ShowMainPageCommand() {
    }

    public static ShowMainPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowMainPageCommand.class) {
                if (instance == null) {
                    instance = new ShowMainPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return mainPageResponse;
    }

}