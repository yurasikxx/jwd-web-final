package com.epam.jwd.command;

public class ShowPersonDeletingPageCommand implements Command {

    public static final String LOGIN_JSP_PATH = "/WEB-INF/jsp/deletingPerson.jsp";

    private static volatile ShowPersonDeletingPageCommand instance;
    private final BaseCommandResponse deletePageResponse = new CommandResponse(LOGIN_JSP_PATH, false);

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
        return deletePageResponse;
    }

}
