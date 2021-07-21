package com.epam.jwd.command;

import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

import static com.epam.jwd.command.ShowPersonListPageCommand.LIST_JSP_PATH;

public class ShowBetslipListPageCommand implements Command {

    protected static final String BETSLIP_ATTRIBUTE_NAME = "betslip";

    private static volatile ShowBetslipListPageCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(LIST_JSP_PATH, false);

    private ShowBetslipListPageCommand() {
        this.betslipService = BetslipService.getInstance();
    }

    public static ShowBetslipListPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipListPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipListPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll();
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, betslips);

        return betslipCommandResponse;
    }

}