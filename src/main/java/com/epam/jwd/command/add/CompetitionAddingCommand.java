package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Objects;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.constant.Constant.AWAY_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.HOME_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.constant.Constant.TEAMS_MUST_BE_DIFFERENT_MSG;
import static com.epam.jwd.constant.Constant.TEAMS_MUST_BE_FROM_THE_SAME_SPORT_MSG;
import static com.epam.jwd.constant.Constant.TEAM_ID_DOES_NOT_EXIST_MSG;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class CompetitionAddingCommand implements Command {

    private static final String COMPETITION_SUCCESSFULLY_ADDED_MSG = "Competition successfully added";
    private static final String COMPETITION_ALREADY_EXISTS_MSG = "Competition with such ID already exists";
    private static final String INVALID_ENTERED_DATA_MSG = ALL_FIELDS_MUST_BE_FILLED_MSG + " or " +
            COMPETITION_ALREADY_EXISTS_MSG.toLowerCase();
    private static final String COMPETITION_ALREADY_EXIST_MSG = "Competition with these teams already exist";

    private static volatile CompetitionAddingCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse;
    private final BaseCommandResponse competitionErrorCommandResponse;

    private CompetitionAddingCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.competitionCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.competitionErrorCommandResponse = competitionCommandResponse;
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

            if (Objects.requireNonNull(getCheckedHomeTeamId(request)) < MIN_LONG_ID_VALUE
                    || Objects.requireNonNull(getCheckedHomeTeamId(request)) > competitionService.findAllTeams().size()
                    || Objects.requireNonNull(getCheckedAwayTeamId(request)) < MIN_LONG_ID_VALUE
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