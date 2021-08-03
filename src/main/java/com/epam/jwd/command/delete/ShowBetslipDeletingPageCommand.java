package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;

public class ShowBetslipDeletingPageCommand implements Command {

    private static final String BETSLIP_DELETING_OPERATION = "Betslip deleting operation";

    private static volatile ShowBetslipDeletingPageCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipDeletingCommandResponse;

    private ShowBetslipDeletingPageCommand() {
        this.betslipService = BetslipService.getInstance();
        this.betslipDeletingCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
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

        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_DELETING_OPERATION);
        request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);

        return betslipDeletingCommandResponse;
    }

}