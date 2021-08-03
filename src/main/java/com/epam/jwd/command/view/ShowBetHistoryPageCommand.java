package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.service.BetHistoryBaseService;
import com.epam.jwd.service.BetHistoryService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.BET_HISTORY_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

public class ShowBetHistoryPageCommand implements Command {

    private static volatile ShowBetHistoryPageCommand instance;

    private final BetHistoryBaseService betHistoryService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(VIEWING_JSP_PATH, false);

    private ShowBetHistoryPageCommand() {
        this.betHistoryService = BetHistoryService.getInstance();
    }

    public static ShowBetHistoryPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetHistoryPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetHistoryPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<BetHistory> bets = betHistoryService.findAll()
                .stream()
                .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());
        request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, bets);

        return betslipCommandResponse;
    }

}