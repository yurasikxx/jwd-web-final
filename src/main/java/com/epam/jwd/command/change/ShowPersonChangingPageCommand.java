package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;

public class ShowPersonChangingPageCommand implements Command {

    private static final String PERSON_CHANGING_OPERATION_MSG = "Person changing operation";

    private static volatile ShowPersonChangingPageCommand instance;

    private final BaseCommandResponse personCommandResponse;

    private ShowPersonChangingPageCommand() {
        this.personCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowPersonChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(PERSON_ATTRIBUTE_NAME, PERSON_CHANGING_OPERATION_MSG);
        return personCommandResponse;
    }

}