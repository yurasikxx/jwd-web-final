package com.epam.jwd.command;

import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class ShowPersonDeletingPageCommand implements Command {

    protected static final String DELETING_JSP_PATH = "/WEB-INF/jsp/deleting.jsp";

    private static final String ENTER_PERSON_ID_MESSAGE = "Enter person ID which needs to be deleted";

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
        request.setAttribute(PERSON_ATTRIBUTE_NAME, ENTER_PERSON_ID_MESSAGE);

        return personDeletingPageResponse;
    }

}