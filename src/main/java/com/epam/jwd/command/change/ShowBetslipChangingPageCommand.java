package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_BET_TYPE_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;

public class ShowBetslipChangingPageCommand implements Command {

    private static final String BETSLIP_CHANGING_MESSAGE_KEY = "betslip.changing";

    private static volatile ShowBetslipChangingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse;

    private ShowBetslipChangingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betslipCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowBetslipChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll();
        final List<Competition> competitions = competitionService.findAll();
        final List<BetType> betTypes = Arrays.stream(BetType.values()).collect(Collectors.toList());

        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_CHANGING_MESSAGE_KEY));
        request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);
        request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);
        request.setAttribute(SELECT_BET_TYPE_ATTRIBUTE_NAME, betTypes);

        return betslipCommandResponse;
    }

}