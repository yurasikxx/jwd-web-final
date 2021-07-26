package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowAllBetsListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class BetDeleteCommand implements Command {

    private static final String WRONG_ENTERED_PERSON_DATA_MSG = "Bet with such id doesn't exist or entered id is non-positive number";
    private static final String BET_SUCCESSFULLY_DELETED_MSG = "Bet successfully deleted";

    private static volatile BetDeleteCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
    private final BaseCommandResponse betErrorCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private BetDeleteCommand() {
        this.betService = BetService.getInstance();
    }

    public static BetDeleteCommand getInstance() {
        if (instance == null) {
            synchronized (BetDeleteCommand.class) {
                if (instance == null) {
                    instance = new BetDeleteCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));

            if (!betService.canBeDeleted(id)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, WRONG_ENTERED_PERSON_DATA_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
                return betErrorCommandResponse;
            }

            betService.delete(id);
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, EMPTY_ID_SENT_MSG);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return betErrorCommandResponse;
        }

        request.setAttribute(BET_ATTRIBUTE_NAME, BET_SUCCESSFULLY_DELETED_MSG);

        return betCommandResponse;
    }

}