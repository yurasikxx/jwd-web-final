package com.epam.jwd.command;

import com.epam.jwd.model.Bet;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import java.util.List;

import static com.epam.jwd.command.ShowBetListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class ShowBetDeletingPageCommand implements Command {

    private static volatile ShowBetDeletingPageCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betDeletingCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowBetDeletingPageCommand() {
        this.betService = BetService.getInstance();
    }

    public static ShowBetDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Bet> bets = betService.findAll();
        request.setAttribute(BET_ATTRIBUTE_NAME, bets);

        return betDeletingCommandResponse;
    }

}