package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Objects;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.AWAY_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ENTER_AWAY_TEAM_ID_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ENTER_HOME_TEAM_ID_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.HOME_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;

public class CompetitionAddingCommand implements Command {

    protected static final Long MIN_ELEMENT_ID = 1L;
    protected static final String SOMETHING_WENT_WRONG_MSG = "Something went wrong...";
    protected static final String ALL_FIELDS_MUST_BE_FILLED_MSG = "All fields must be filled";

    private static final String COMPETITION_SUCCESSFULLY_ADDED_MSG = "Competition successfully added";
    private static final String HOME_TEAM_PARAMETER_NAME = "homeTeamId";
    private static final String AWAY_TEAM_PARAMETER_NAME = "awayTeamId";
    private static final String COMPETITION_ALREADY_EXISTS_MSG = "Competition with such ID already exists";
    private static final String INVALID_ENTERED_DATA_MSG = ALL_FIELDS_MUST_BE_FILLED_MSG + " or " +
            COMPETITION_ALREADY_EXISTS_MSG.toLowerCase();
    private static final String TEAM_ID_DOES_NOT_EXIST_MSG = "Team with such ID doesn't exist";
    private static final String TEAMS_MUST_BE_DIFFERENT_MSG = "Teams must be different";
    private static final String TEAMS_MUST_BE_FROM_THE_SAME_SPORT_MSG = "Teams must be from the same sport";
    private static final String COMPETITION_ALREADY_EXIST_MSG = "Competition with these teams already exist";

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
            if (getCheckedHomeTeamId(request) == null || getCheckedAwayTeamId(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, INVALID_ENTERED_DATA_MSG);
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return competitionErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedHomeTeamId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedHomeTeamId(request)) > competitionService.findAllTeams().size()
                    || Objects.requireNonNull(getCheckedAwayTeamId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedAwayTeamId(request)) > competitionService.findAllTeams().size()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, TEAM_ID_DOES_NOT_EXIST_MSG);
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return competitionErrorCommandResponse;
            }

            if (Objects.equals(getCheckedHomeTeamId(request), getCheckedAwayTeamId(request))) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, TEAMS_MUST_BE_DIFFERENT_MSG);
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return competitionErrorCommandResponse;
            }

            if (competitionService.findTeamById(getCheckedHomeTeamId(request)).getSport()
                    != competitionService.findTeamById(getCheckedAwayTeamId(request)).getSport()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, TEAMS_MUST_BE_FROM_THE_SAME_SPORT_MSG);
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return competitionErrorCommandResponse;
            }

            final Long homeTeamId = getCheckedHomeTeamId(request);
            final Long awayTeamId = getCheckedAwayTeamId(request);

            final Competition competition = new Competition(
                    competitionService.findTeamById(homeTeamId),
                    competitionService.findTeamById(awayTeamId)
            );

            if (!competitionService.canSave(competition)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, COMPETITION_ALREADY_EXIST_MSG);
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return competitionErrorCommandResponse;
            }

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
        request.setAttribute(HOME_TEAM_ATTRIBUTE_NAME, ENTER_HOME_TEAM_ID_MSG);
        request.setAttribute(AWAY_TEAM_ATTRIBUTE_NAME, ENTER_AWAY_TEAM_ID_MSG);

        return competitionCommandResponse;
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