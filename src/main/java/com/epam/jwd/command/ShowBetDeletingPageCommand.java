package com.epam.jwd.command;

import static com.epam.jwd.command.ShowAllBetsListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class ShowBetDeletingPageCommand implements Command {

    private static final String ENTER_BET_ID_MESSAGE = "Enter bet ID which needs to be deleted";
    private static volatile ShowBetDeletingPageCommand instance;

    private final BaseCommandResponse betDeletingCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowBetDeletingPageCommand() {
    }

    public static ShowBetDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_ATTRIBUTE_NAME, ENTER_BET_ID_MESSAGE);
        return betDeletingCommandResponse;
    }

}