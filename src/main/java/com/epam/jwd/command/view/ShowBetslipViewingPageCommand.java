package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

public class ShowBetslipViewingPageCommand implements Command {

    private static volatile ShowBetslipViewingPageCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(VIEWING_JSP_PATH, false);

    private ShowBetslipViewingPageCommand() {
        this.betslipService = BetslipService.getInstance();
    }

    public static ShowBetslipViewingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipViewingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipViewingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll()
                .stream()
                .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, betslips);

        return betslipCommandResponse;
    }

}