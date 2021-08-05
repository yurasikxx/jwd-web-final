package com.epam.jwd.command.delete;

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

import java.util.List;

import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_EMPTY_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

public class CompetitionDeletingCommand implements Command {

    private static final String COMPETITION_SELECT_MESSAGE_KEY = "competition.not.selected";
    private static final String COMPETITION_CANNOT_DELETE_MESSAGE_KEY = "competition.cannot.delete";
    private static final String COMPETITION_DELETED_MESSAGE_KEY = "competition.deleted";

    private static volatile CompetitionDeletingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private CompetitionDeletingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
    }

    public static CompetitionDeletingCommand getInstance() {
        if (instance == null) {
            synchronized (CompetitionDeletingCommand.class) {
                if (instance == null) {
                    instance = new CompetitionDeletingCommand();
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
            final Long id = getCheckedId(request);

            if (!competitionService.canBeDeleted(id)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_SELECT_MESSAGE_KEY));
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return competitionCommandResponse;
            }

            competitionService.delete(id);

            final List<Competition> competitions = competitionService.findAll();

            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_DELETED_MESSAGE_KEY));
            request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ID_EMPTY_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_CANNOT_DELETE_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        }

        return competitionCommandResponse;
    }

    private Long getCheckedId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

}