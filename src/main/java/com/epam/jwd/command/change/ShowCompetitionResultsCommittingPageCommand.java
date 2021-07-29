package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.BET_HISTORY_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;

public class ShowCompetitionResultsCommittingPageCommand implements Command {

    private static final String COMPETITION_RESULTS_COMMITTING_OPERATION_MSG = "Competition results committing operation";

    private static volatile ShowCompetitionResultsCommittingPageCommand instance;

    private final BaseCommandResponse betHistoryCommandResponse;

    private ShowCompetitionResultsCommittingPageCommand() {
        this.betHistoryCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowCompetitionResultsCommittingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionResultsCommittingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionResultsCommittingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, COMPETITION_RESULTS_COMMITTING_OPERATION_MSG);
        return betHistoryCommandResponse;
    }

}