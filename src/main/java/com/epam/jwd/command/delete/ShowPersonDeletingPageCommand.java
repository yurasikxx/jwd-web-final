package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;

public class ShowPersonDeletingPageCommand implements Command {

    private static final String PERSON_DELETING_OPERATION_MSG = "Person deleting operation";

    private static volatile ShowPersonDeletingPageCommand instance;

    private final BaseCommandResponse personDeletingPageResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowPersonDeletingPageCommand() {
    }

    public static ShowPersonDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(PERSON_ATTRIBUTE_NAME, PERSON_DELETING_OPERATION_MSG);

        return personDeletingPageResponse;
    }

}