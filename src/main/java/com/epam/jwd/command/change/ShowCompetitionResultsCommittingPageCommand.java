package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.BET_HISTORY_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;

public class ShowCompetitionResultsCommittingPageCommand implements Command {

    private static final String COMPETITION_RESULTS_COMMITTING_OPERATION_MSG = "Competition results committing operation";

    private static volatile ShowCompetitionResultsCommittingPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse betHistoryCommandResponse;

    private ShowCompetitionResultsCommittingPageCommand() {
        this.betHistoryCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
        this.competitionService = CompetitionService.getInstance();
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
        final List<Competition> competitions = competitionService.findAll();

        request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, COMPETITION_RESULTS_COMMITTING_OPERATION_MSG);
        request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);

        return betHistoryCommandResponse;
    }

}