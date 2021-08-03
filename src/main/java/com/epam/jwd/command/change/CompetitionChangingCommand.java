package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Team;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.constant.Constant.AWAY_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.HOME_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.constant.Constant.TEAMS_MUST_BE_DIFFERENT_MSG;
import static com.epam.jwd.constant.Constant.TEAMS_MUST_BE_FROM_THE_SAME_SPORT_MSG;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class CompetitionChangingCommand implements Command {

    private static final String COMPETITION_SUCCESSFULLY_CHANGED_MSG = "Competition successfully changed";
    private static final String COMPETITION_OR_TEAMS_NOT_SELECTED_MSG = "Competition or teams not selected";

    private static volatile CompetitionChangingCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse;

    private CompetitionChangingCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.competitionCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
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
        return getCommandResponse(request);
    }

    private BaseCommandResponse getCommandResponse(BaseCommandRequest request) {
        return getBaseCommandResponse(request);
    }

    private BaseCommandResponse getBaseCommandResponse(BaseCommandRequest request) {
        try {
            final Long id = getCheckedId(request);
            final Long homeTeamId = getCheckedHomeTeamId(request);
            final Long awayTeamId = getCheckedAwayTeamId(request);

            if (cannotBeChanged(request, id, homeTeamId, awayTeamId)) {
                return competitionCommandResponse;
            }

            final Competition competition = new Competition(id,
                    competitionService.findTeamById(homeTeamId),
                    competitionService.findTeamById(awayTeamId));

            competitionService.update(competition);

            final List<Team> teams = competitionService.findAllTeams();
            final List<Competition> competitions = competitionService.findAll();

            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_SUCCESSFULLY_CHANGED_MSG);
            request.setAttribute(SELECT_TEAM_ATTRIBUTE_NAME, teams);
            request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);

            return competitionCommandResponse;
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return competitionCommandResponse;
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return competitionCommandResponse;
        }
    }

    private boolean cannotBeChanged(BaseCommandRequest request, Long id, Long homeTeamId, Long awayTeamId) throws ServiceException, DaoException {
        if (id < MIN_LONG_ID_VALUE || homeTeamId < MIN_LONG_ID_VALUE || awayTeamId < MIN_LONG_ID_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, COMPETITION_OR_TEAMS_NOT_SELECTED_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return true;
        }

        if (homeTeamId.equals(awayTeamId)) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, TEAMS_MUST_BE_DIFFERENT_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return true;
        }

        if (competitionService.findTeamById(homeTeamId).getSport()
                != competitionService.findTeamById(awayTeamId).getSport()) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, TEAMS_MUST_BE_FROM_THE_SAME_SPORT_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return true;
        }

        return false;
    }

    private Long getCheckedId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

    private Long getCheckedHomeTeamId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(HOME_TEAM_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(HOME_TEAM_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

    private Long getCheckedAwayTeamId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(AWAY_TEAM_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(AWAY_TEAM_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

}