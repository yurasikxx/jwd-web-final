package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

import static com.epam.jwd.constant.Constant.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class BetslipDeletingCommand implements Command {

    private static final String BETSLIP_NOT_SELECTED_MSG = "Betslip not selected";
    private static final String BETSLIP_CANNOT_BE_DELETED_MSG = "Betslip cannot be deleted while there is unplayed bet";
    private static final String BETSLIP_SUCCESSFULLY_DELETED_MSG = "Betslip successfully deleted";

    private static volatile BetslipDeletingCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
    private final BaseCommandResponse betslipErrorCommandResponse = betslipCommandResponse;

    private BetslipDeletingCommand() {
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
                request.setAttribute(ERROR_ATTRIBUTE_NAME, BETSLIP_NOT_SELECTED_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
                return betslipErrorCommandResponse;
            }

            betslipService.delete(id);

            final List<Betslip> betslips = betslipService.findAll();

            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_SUCCESSFULLY_DELETED_MSG);
            request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);

            return betslipCommandResponse;
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, EMPTY_ID_SENT_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipErrorCommandResponse;
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, BETSLIP_CANNOT_BE_DELETED_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipErrorCommandResponse;
        }
    }

    private Long getCheckedId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

}