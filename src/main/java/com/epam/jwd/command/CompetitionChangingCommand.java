package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Objects;

import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.AWAY_TEAM_PARAMETER_NAME;
import static com.epam.jwd.command.CompetitionAddingCommand.HOME_TEAM_PARAMETER_NAME;
import static com.epam.jwd.command.CompetitionAddingCommand.MIN_ELEMENT_ID;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.TEAMS_MUST_BE_DIFFERENT_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.TEAMS_MUST_BE_FROM_THE_SAME_SPORT_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.TEAM_ID_DOES_NOT_EXIST_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.AWAY_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ENTER_AWAY_TEAM_ID_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ENTER_HOME_TEAM_ID_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.HOME_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionChangingPageCommand.ENTER_ID_OF_CHANGEABLE_COMPETITION_MSG;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.CHANGING_JSP_PATH;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.ID_ATTRIBUTE_NAME;

public class CompetitionChangingCommand implements Command {

    private static final String COMPETITION_SUCCESSFULLY_CHANGED_MSG = "Competition successfully changed";
    private static final String COMPETITION_DOES_NOT_EXIST_MSG = "Competition with such ID doesn't exist";

    private static volatile CompetitionChangingCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse;
    private final BaseCommandResponse competitionErrorCommandResponse;

    private CompetitionChangingCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.competitionCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
        this.competitionErrorCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static CompetitionChangingCommand getInstance() {
        if (instance == null) {
            synchronized (CompetitionChangingCommand.class) {
                if (instance == null) {
                    instance = new CompetitionChangingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedId(request) == null
                    || getCheckedHomeTeamId(request) == null
                    || getCheckedAwayTeamId(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
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

            if (Objects.requireNonNull(getCheckedId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedId(request)) > competitionService.findAll().size()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, COMPETITION_DOES_NOT_EXIST_MSG);
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

            final Long id = getCheckedId(request);
            final Long homeTeamId = getCheckedHomeTeamId(request);
            final Long awayTeamId = getCheckedAwayTeamId(request);

            final Competition competition = new Competition(id,
                    competitionService.findTeamById(homeTeamId),
                    competitionService.findTeamById(awayTeamId)
            );

            competitionService.update(competition);
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return competitionErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return competitionErrorCommandResponse;
        }

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_SUCCESSFULLY_CHANGED_MSG);
        request.setAttribute(ID_ATTRIBUTE_NAME, ENTER_ID_OF_CHANGEABLE_COMPETITION_MSG);
        request.setAttribute(HOME_TEAM_ATTRIBUTE_NAME, ENTER_HOME_TEAM_ID_MSG);
        request.setAttribute(AWAY_TEAM_ATTRIBUTE_NAME, ENTER_AWAY_TEAM_ID_MSG);

        return competitionCommandResponse;
    }

    private Long getCheckedId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
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
