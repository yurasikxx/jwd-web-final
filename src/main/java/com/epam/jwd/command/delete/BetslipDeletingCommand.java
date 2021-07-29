package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class BetslipDeletingCommand implements Command {

    private static final String WRONG_ENTERED_PERSON_DATA_MSG = "Betslip with such id doesn't exist or entered id is non-positive number";
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
        try {
            final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));

            if (!betslipService.canBeDeleted(id)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, WRONG_ENTERED_PERSON_DATA_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
                return betslipErrorCommandResponse;
            }

            betslipService.delete(id);
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTRIBUTE_NAME, BETSLIP_CANNOT_BE_DELETED_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return betslipErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, EMPTY_ID_SENT_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return betslipErrorCommandResponse;
        }

        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_SUCCESSFULLY_DELETED_MSG);

        return betslipCommandResponse;
    }

}