package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;

public class ShowBetChangingPageCommand implements Command {

    private static final String BET_CHANGING_OPERATION_MSG = "Bet changing operation";

    private static volatile ShowBetChangingPageCommand instance;

    private final BaseCommandResponse betCommandResponse;

    private ShowBetChangingPageCommand() {
        this.betCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowBetChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_ATTRIBUTE_NAME, BET_CHANGING_OPERATION_MSG);
        return betCommandResponse;
    }

}