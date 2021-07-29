package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;

public class ShowBetAddingPageCommand implements Command {

    private static final String BET_ADDING_OPERATION_MSG = "Bet adding operation\n";

    private static volatile ShowBetAddingPageCommand instance;

    private final BaseCommandResponse betCommandResponse;

    private ShowBetAddingPageCommand() {
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowBetAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_ATTRIBUTE_NAME, BET_ADDING_OPERATION_MSG);

        return betCommandResponse;
    }

}