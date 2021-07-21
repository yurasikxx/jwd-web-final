package com.epam.jwd.command;

import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class ShowBetslipDeletingPageCommand implements Command {

    private static volatile ShowBetslipDeletingPageCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipDeletingCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowBetslipDeletingPageCommand() {
        this.betslipService = BetslipService.getInstance();
    }

    public static ShowBetslipDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll();
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, betslips);

        return betslipDeletingCommandResponse;
    }

}