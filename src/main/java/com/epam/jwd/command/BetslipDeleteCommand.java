package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class BetslipDeleteCommand implements Command {

    private static final String WRONG_ENTERED_PERSON_DATA_MSG = "Betslip with such id doesn't exist or entered id is non-positive number";
    private static final String BETSLIP_CANNOT_BE_DELETED_MSG = "Betslip cannot be deleted while there is unplayed bet";
    private static final String BETSLIP_SUCCESSFULLY_DELETED_MSG = "Betslip successfully deleted";

    private static volatile BetslipDeleteCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
    private final BaseCommandResponse betslipErrorCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private BetslipDeleteCommand() {
        this.betslipService = BetslipService.getInstance();
    }

    public static BetslipDeleteCommand getInstance() {
        if (instance == null) {
            synchronized (BetslipDeleteCommand.class) {
                if (instance == null) {
                    instance = new BetslipDeleteCommand();
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