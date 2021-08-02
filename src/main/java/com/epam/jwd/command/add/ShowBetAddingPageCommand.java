package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;

public class ShowBetAddingPageCommand implements Command {

    private static final String BET_ADDING_OPERATION_MSG = "Bet adding operation\n";
    private static final String SELECT_BETSLIPS_ATTRIBUTE_NAME = "selectBetslip";

    private static volatile ShowBetAddingPageCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betCommandResponse;

    private ShowBetAddingPageCommand() {
        this.betslipService = BetslipService.getInstance();
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowBetAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll();

        request.setAttribute(BET_ATTRIBUTE_NAME, BET_ADDING_OPERATION_MSG);
        request.setAttribute(SELECT_BETSLIPS_ATTRIBUTE_NAME, betslips);

        return betCommandResponse;
    }

}