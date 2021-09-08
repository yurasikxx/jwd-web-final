package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_EMPTY_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code CompetitionDeletingCommand} class implements {@code Command}
 * interface and execute command that deletes competition.
 *
 * @see Command
 */
public class CompetitionDeletingCommand implements Command {

    private static final String COMPETITION_SELECT_MESSAGE_KEY = "competition.not.selected";
    private static final String COMPETITION_CANNOT_DELETE_MESSAGE_KEY = "competition.cannot.delete";

    private static volatile CompetitionDeletingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse successDeletingCommandResponse;
    private final BaseCommandResponse errorDeletingCommandResponse;

    private CompetitionDeletingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.successDeletingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorDeletingCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
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

                return errorDeletingCommandResponse;
            }

            competitionService.delete(id);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorDeletingCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ID_EMPTY_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorDeletingCommandResponse;
        } catch (ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_CANNOT_DELETE_MESSAGE_KEY));
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorDeletingCommandResponse;
        }

        return successDeletingCommandResponse;
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