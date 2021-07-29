package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Bet;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import java.util.List;

import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

public class ShowAllBetsViewingPageCommand implements Command {

    private static volatile ShowAllBetsViewingPageCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(VIEWING_JSP_PATH, false);

    private ShowAllBetsViewingPageCommand() {
        this.betService = BetService.getInstance();
    }

    public static ShowAllBetsViewingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowAllBetsViewingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowAllBetsViewingPageCommand();
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