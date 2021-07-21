package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import java.util.List;

import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.ShowBetListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class BetDeleteCommand implements Command {

    private static volatile BetDeleteCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

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
        final List<Bet> bets = betService.findAll();
        request.setAttribute(BET_ATTRIBUTE_NAME, bets);
        final String id = request.getParameter(ID_PARAMETER_NAME);

        try {
            betService.delete(Long.parseLong(id));
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
        }

        return betCommandResponse;
    }

}