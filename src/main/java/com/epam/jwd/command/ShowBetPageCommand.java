package com.epam.jwd.command;

import com.epam.jwd.model.Bet;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import java.util.List;

public class ShowBetPageCommand implements Command {

    private static final String BET_JSP_PATH = "/WEB-INF/jsp/bet.jsp";
    private static final String BET_ATTRIBUTE_NAME = "bet";

    private static volatile ShowBetPageCommand instance;

    private final BaseCommandResponse betCommandResponse = new CommandResponse(BET_JSP_PATH, false);
    private final BetBaseService betService;

    private ShowBetPageCommand() {
        this.betService = BetService.getInstance();
    }

    public static ShowBetPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Bet> bets = betService.findAll();
        request.setAttribute(BET_ATTRIBUTE_NAME, bets);

        return betCommandResponse;
    }

}