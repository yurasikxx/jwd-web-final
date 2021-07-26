package com.epam.jwd.command;

import com.epam.jwd.model.Bet;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import java.util.List;

import static com.epam.jwd.command.ShowPersonListPageCommand.LIST_JSP_PATH;

public class ShowAllBetsListPageCommand implements Command {

    protected static final String BET_ATTRIBUTE_NAME = "bet";

    private static volatile ShowAllBetsListPageCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(LIST_JSP_PATH, false);

    private ShowAllBetsListPageCommand() {
        this.betService = BetService.getInstance();
    }

    public static ShowAllBetsListPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowAllBetsListPageCommand.class) {
                if (instance == null) {
                    instance = new ShowAllBetsListPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Bet> bets = betService.findAll();
        request.setAttribute(BET_ATTRIBUTE_NAME, bets);

        return betslipCommandResponse;
    }

}