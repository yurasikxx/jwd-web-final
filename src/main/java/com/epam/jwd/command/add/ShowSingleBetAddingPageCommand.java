package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SINGLE_BET_ATTRIBUTE_NAME;

/**
 * A {@code ShowSingleBetAddingPageCommand} class implements {@code Command}
 * interface and execute command that showing person single bet page.
 *
 * @see Command
 */
public class ShowSingleBetAddingPageCommand implements Command {

    private static final String SINGLE_BET_ADDING_MESSAGE_KEY = "bet.single.adding";

    private static volatile ShowSingleBetAddingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betCommandResponse;

    private ShowSingleBetAddingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowSingleBetAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowSingleBetAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowSingleBetAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll()
                .stream()
                .sorted(Comparator.comparing(o -> o.getCompetition().toString()))
                .collect(Collectors.toList());

        request.setAttribute(SINGLE_BET_ATTRIBUTE_NAME, messageManager.getString(SINGLE_BET_ADDING_MESSAGE_KEY));
        request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);

        return betCommandResponse;
    }

}