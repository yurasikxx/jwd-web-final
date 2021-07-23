package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.AWAY_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.HOME_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.SPORT_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;

public class CompetitionAddingCommand implements Command {

    private static final String SOMETHING_WENT_WRONG_MSG = "Something went wrong...";
    private static final String COMPETITION_SUCCESSFULLY_ADDED_MSG = "Competition successfully added";
    private static volatile CompetitionAddingCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse;
    private final BaseCommandResponse competitionErrorCommandResponse;

    private CompetitionAddingCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.competitionCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.competitionErrorCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static CompetitionAddingCommand getInstance() {
        if (instance == null) {
            synchronized (CompetitionAddingCommand.class) {
                if (instance == null) {
                    instance = new CompetitionAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            final Long sportId = Long.valueOf(request.getParameter(SPORT_ATTRIBUTE_NAME));
            final Long homeTeamId = Long.valueOf(request.getParameter(HOME_TEAM_ATTRIBUTE_NAME));
            final Long awayTeamId = Long.valueOf(request.getParameter(AWAY_TEAM_ATTRIBUTE_NAME));
            final Competition competition = new Competition(Sport.resolveSportById(sportId),
                    competitionService.findTeamById(homeTeamId),
                    competitionService.findTeamById(awayTeamId));

            competitionService.save(competition);
        } catch (DaoException | ServiceException e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            return competitionErrorCommandResponse;
        }

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_SUCCESSFULLY_ADDED_MSG);

        return competitionCommandResponse;
    }

}