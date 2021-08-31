package com.epam.jwd.command.change;

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

import static com.epam.jwd.constant.Constant.AWAY_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.HOME_TEAM_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.TEAM_DIFFERENCE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.TEAM_SPORT_DIFFERENCE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code CompetitionChangingCommand} class implements {@code Command}
 * interface and execute command that changes competition.
 *
 * @see Command
 */
public class CompetitionChangingCommand implements Command {

    private static final String COMPETITION_TEAM_SELECT_MESSAGE_KEY = "competition.team.select";

    private static volatile CompetitionChangingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse successChangingCommandResponse;
    private final BaseCommandResponse errorChangingCommandResponse;

    private CompetitionChangingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.successChangingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorChangingCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
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
                return errorChangingCommandResponse;
            }

            final Competition competition = new Competition(id,
                    competitionService.findTeamById(homeTeamId),
                    competitionService.findTeamById(awayTeamId));

            competitionService.update(competition);
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorChangingCommandResponse;
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorChangingCommandResponse;
        }

        return successChangingCommandResponse;
    }

    private boolean cannotBeChanged(BaseCommandRequest request, Long id, Long homeTeamId, Long awayTeamId)
            throws ServiceException, DaoException {
        if (id < MIN_LONG_ID_VALUE || homeTeamId < MIN_LONG_ID_VALUE || awayTeamId < MIN_LONG_ID_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_TEAM_SELECT_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (homeTeamId.equals(awayTeamId)) {
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

    private Long getCheckedId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
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