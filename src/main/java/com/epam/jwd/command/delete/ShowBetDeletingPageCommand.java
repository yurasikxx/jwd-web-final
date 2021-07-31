package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;

public class ShowBetDeletingPageCommand implements Command {

    private static final String BET_DELETING_OPERATION_MSG = "Bet deleting operation";

    private static volatile ShowBetDeletingPageCommand instance;

    private final BaseCommandResponse betDeletingCommandResponse;

    private ShowBetDeletingPageCommand() {
        this.betDeletingCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
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
        request.setAttribute(BET_ATTRIBUTE_NAME, BET_DELETING_OPERATION_MSG);
        return betDeletingCommandResponse;
    }

}