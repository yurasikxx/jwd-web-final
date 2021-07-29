package com.epam.jwd.command.management;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

public class ShowPersonManagementPageCommand implements Command {

    private static final String PERSON_JSP_PATH = "/jsp/person.jsp";

    private static volatile ShowPersonManagementPageCommand instance;

    private final BaseCommandResponse personCommandResponse = new CommandResponse(PERSON_JSP_PATH, false);

    private ShowPersonManagementPageCommand() {
    }

    public static ShowPersonManagementPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonManagementPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonManagementPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return personCommandResponse;
    }

}