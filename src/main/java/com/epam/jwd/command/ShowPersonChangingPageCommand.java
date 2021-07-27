package com.epam.jwd.command;

import static com.epam.jwd.command.ShowPersonAddingPageCommand.ENTER_PERSON_LOGIN_MSG;
import static com.epam.jwd.command.ShowPersonAddingPageCommand.ENTER_PERSON_PASSWORD_MSG;
import static com.epam.jwd.command.ShowPersonAddingPageCommand.LOGIN_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonAddingPageCommand.PASSWORD_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class ShowPersonChangingPageCommand implements Command {

    protected static final String ENTER_ID_OF_CHANGEABLE_PERSON_MSG = "Enter ID of changeable person: ";
    protected static final String ID_ATTRIBUTE_NAME = "id";
    protected static final String CHANGING_JSP_PATH = "/WEB-INF/jsp/changing.jsp";
    protected static final String BALANCE_ATTRIBUTE_NAME = "balance";
    protected static final String ENTER_PERSON_BALANCE_MSG = "Enter person balance: ";

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
        request.setAttribute(ID_ATTRIBUTE_NAME, ENTER_ID_OF_CHANGEABLE_PERSON_MSG);
        request.setAttribute(LOGIN_ATTRIBUTE_NAME, ENTER_PERSON_LOGIN_MSG);
        request.setAttribute(PASSWORD_ATTRIBUTE_NAME, ENTER_PERSON_PASSWORD_MSG);
        request.setAttribute(BALANCE_ATTRIBUTE_NAME, ENTER_PERSON_BALANCE_MSG);

        return personCommandResponse;
    }

}