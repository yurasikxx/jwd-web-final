package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class BetDeletingCommand implements Command {

    private static final String WRONG_ENTERED_PERSON_DATA_MSG = "Bet with such id doesn't exist or entered id is non-positive number";
    private static final String BET_SUCCESSFULLY_DELETED_MSG = "Bet successfully deleted";

    private static volatile BetDeletingCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
    private final BaseCommandResponse betErrorCommandResponse = betCommandResponse;

    private BetDeletingCommand() {
        this.betService = BetService.getInstance();
    }

    public static BetDeletingCommand getInstance() {
        if (instance == null) {
            synchronized (BetDeletingCommand.class) {
                if (instance == null) {
                    instance = new BetDeletingCommand();
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