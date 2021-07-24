package com.epam.jwd.command;

import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;
import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class ShowPersonAddingPageCommand implements Command {

    private static final String PERSON_ADDING_OPERATION_MSG = "Person adding operation";
    private static final String LOGIN_ATTRIBUTE_NAME = "login";
    private static final String ENTER_PERSON_LOGIN_MSG = "Enter person login: ";
    private static final String PASSWORD_ATTRIBUTE_NAME = "password";
    private static final String ENTER_PERSON_PASSWORD_MSG = "Enter person password: ";
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
        request.setAttribute(LOGIN_ATTRIBUTE_NAME, ENTER_PERSON_LOGIN_MSG);
        request.setAttribute(PASSWORD_ATTRIBUTE_NAME, ENTER_PERSON_PASSWORD_MSG);

        return personCommandResponse;
    }

}