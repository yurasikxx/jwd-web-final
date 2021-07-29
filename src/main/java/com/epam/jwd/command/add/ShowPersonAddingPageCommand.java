package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;

public class ShowPersonAddingPageCommand implements Command {

    private static final String PERSON_ADDING_OPERATION_MSG = "Person adding operation";
    private static volatile ShowPersonAddingPageCommand instance;

    private final BaseCommandResponse personCommandResponse;

    private ShowPersonAddingPageCommand() {
        this.personCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowPersonAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(PERSON_ATTRIBUTE_NAME, PERSON_ADDING_OPERATION_MSG);
        return personCommandResponse;
    }

}