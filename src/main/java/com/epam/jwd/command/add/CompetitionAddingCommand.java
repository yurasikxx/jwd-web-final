package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Objects;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.AWAY_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.COMPETITION_ALREADY_EXISTS_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.HOME_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.TEAM_DIFFERENCE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.TEAM_SPORT_DIFFERENCE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code CompetitionAddingCommand} class implements {@code Command}
 * interface and execute command that adds competition.
 *
 * @see Command
 */
public class CompetitionAddingCommand implements Command {

    private static final String TEAM_SELECT_MESSAGE_KEY = "team.select";

    private static volatile CompetitionAddingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse successAddingCommandResponse;
    private final BaseCommandResponse errorAddingCommandResponse;

    private CompetitionAddingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.successAddingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorAddingCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
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
        return getCommandResponse(request);
    }

    private BaseCommandResponse getCommandResponse(BaseCommandRequest request) {
        try {
            final Long homeTeamId = getCheckedHomeTeamId(request);
            final Long awayTeamId = getCheckedAwayTeamId(request);

            if (cannotBeAdded(request, homeTeamId, awayTeamId)) {
                return errorAddingCommandResponse;
            }

            final Competition competition = new Competition(
                    competitionService.findTeamById(homeTeamId),
                    competitionService.findTeamById(awayTeamId));

            if (!competitionService.canSave(competition)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_ALREADY_EXISTS_MESSAGE_KEY));
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return errorAddingCommandResponse;
            }

            competitionService.save(competition);
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        }

        return successAddingCommandResponse;
    }

    private boolean cannotBeAdded(BaseCommandRequest request, Long homeTeamId, Long awayTeamId) throws DaoException, ServiceException {
        if (homeTeamId < MIN_LONG_ID_VALUE || awayTeamId < MIN_LONG_ID_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(TEAM_SELECT_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (Objects.equals(homeTeamId, awayTeamId)) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(TEAM_DIFFERENCE_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (competitionService.findTeamById(homeTeamId).getSport()
                != competitionService.findTeamById(awayTeamId).getSport()) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(TEAM_SPORT_DIFFERENCE_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        return false;
    }

    private Long getCheckedHomeTeamId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(HOME_TEAM_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(HOME_TEAM_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private Long getCheckedAwayTeamId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(AWAY_TEAM_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(AWAY_TEAM_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

}