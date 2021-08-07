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
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_EMPTY_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code BetslipDeletingCommand} class implements {@code Command}
 * interface and execute command that deletes betslip.
 *
 * @see Command
 */
public class BetslipDeletingCommand implements Command {

    private static final String BETSLIP_SELECT_MESSAGE_KEY = "betslip.not.selected";
    private static final String BETSLIP_CANNOT_DELETE_MESSAGE_KEY = "betslip.cannot.delete";
    private static final String BETSLIP_DELETED_MESSAGE_KEY = "betslip.deleted";

    private static volatile BetslipDeletingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private BetslipDeletingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.betslipService = BetslipService.getInstance();
    }

    public static BetslipDeletingCommand getInstance() {
        if (instance == null) {
            synchronized (BetslipDeletingCommand.class) {
                if (instance == null) {
                    instance = new BetslipDeletingCommand();
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

            if (!betslipService.canBeDeleted(id)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_SELECT_MESSAGE_KEY));
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return betslipCommandResponse;
            }

            betslipService.delete(id);

            final List<Betslip> betslips = betslipService.findAll();

            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_DELETED_MESSAGE_KEY));
            request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ID_EMPTY_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_CANNOT_DELETE_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        }

        return betslipCommandResponse;
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