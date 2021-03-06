package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.Bet;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

/**
 * A {@code ShowAllBetsViewingPageCommand} class implements {@code Command}
 * interface and execute command that showing all bets viewing page.
 *
 * @see Command
 */
public class ShowAllBetsViewingPageCommand implements Command {

    private static volatile ShowAllBetsViewingPageCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse;

    private ShowAllBetsViewingPageCommand() {
        this.betService = BetService.getInstance();
        this.betCommandResponse = new CommandResponse(VIEWING_JSP_PATH, false);
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
        final List<Bet> bets = betService.findAll()
                .stream()
                .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());

        request.setAttribute(BET_ATTRIBUTE_NAME, bets);

        return betCommandResponse;
    }

}