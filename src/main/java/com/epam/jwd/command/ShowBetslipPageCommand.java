package com.epam.jwd.command;

import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

public class ShowBetslipPageCommand implements Command {

    private static final String BETSLIP_JSP_PATH = "/WEB-INF/jsp/betslip.jsp";
    private static final String BETSLIP_ATTRIBUTE_NAME = "betslip";

    private static volatile ShowBetslipPageCommand instance;

    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(BETSLIP_JSP_PATH, false);
    private final BetslipBaseService betslipService;

    private ShowBetslipPageCommand() {
        this.betslipService = BetslipService.getInstance();
    }

    public static ShowBetslipPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipPageCommand();
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