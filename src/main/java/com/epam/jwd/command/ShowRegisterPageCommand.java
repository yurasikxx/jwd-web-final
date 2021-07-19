package com.epam.jwd.command;

public class ShowRegisterPageCommand implements Command {

    public static final String REGISTER_JSP_PATH = "/WEB-INF/jsp/register.jsp";

    private static volatile ShowRegisterPageCommand instance;
    private final BaseCommandResponse registerCommandResponse = new CommandResponse(REGISTER_JSP_PATH, false);

    private ShowRegisterPageCommand() {
    }

    public static ShowRegisterPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowRegisterPageCommand.class) {
                if (instance == null) {
                    instance = new ShowRegisterPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return registerCommandResponse;
    }

}