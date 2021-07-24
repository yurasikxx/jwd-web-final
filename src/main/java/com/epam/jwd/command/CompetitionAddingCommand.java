package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;

public class CompetitionAddingCommand implements Command {

    protected static final String SOMETHING_WENT_WRONG_MSG = "Something went wrong...";
    protected static final String ALL_FIELDS_MUST_BE_FILLED_MSG = "All fields must be filled!";

    private static final String COMPETITION_SUCCESSFULLY_ADDED_MSG = "Competition successfully added";
    private static final String SPORT_PARAMETER_NAME = "sportId";
    private static final String HOME_TEAM_PARAMETER_NAME = "homeTeamId";
    private static final String AWAY_TEAM_PARAMETER_NAME = "awayTeamId";
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
            if (getCheckedSportId(request) == null
                    || getCheckedHomeTeamId(request) == null
                    || getCheckedAwayTeamId(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return competitionErrorCommandResponse;
            }

            final Long sportId = getCheckedSportId(request);
            final Long homeTeamId = getCheckedHomeTeamId(request);
            final Long awayTeamId = getCheckedAwayTeamId(request);

            final Competition competition = new Competition(Sport.resolveSportById(sportId),
                    competitionService.findTeamById(homeTeamId),
                    competitionService.findTeamById(awayTeamId));

            competitionService.save(competition);
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return competitionErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return competitionErrorCommandResponse;
        }

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_SUCCESSFULLY_ADDED_MSG);

        return competitionCommandResponse;
    }

    private Long getCheckedSportId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(SPORT_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(SPORT_PARAMETER_NAME));
            return id;
        }

        return null;
    }

    private Long getCheckedHomeTeamId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(HOME_TEAM_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(HOME_TEAM_PARAMETER_NAME));
            return id;
        }

        return null;
    }

    private Long getCheckedAwayTeamId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(AWAY_TEAM_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(AWAY_TEAM_PARAMETER_NAME));
            return id;
        }

        return null;
    }

}