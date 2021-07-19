package com.epam.jwd.command;

public class ShowErrorPageCommand implements Command {

    private static final String ERROR_JSP_PATH = "/WEB-INF/jsp/error.jsp";

    private static volatile ShowErrorPageCommand instance;
    private final BaseCommandResponse errorCommandResponse = new CommandResponse(ERROR_JSP_PATH, false);

    private ShowErrorPageCommand() {
    }

    public static ShowErrorPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowErrorPageCommand.class) {
                if (instance == null) {
                    instance = new ShowErrorPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return errorCommandResponse;
    }

}