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
import static com.epam.jwd.constant.Constant.SYSTEM_BET_ATTRIBUTE_NAME;

/**
 * A {@code ShowSystemBetAddingPageCommand} class implements {@code Command}
 * interface and execute command that showing person system bet page.
 *
 * @see Command
 */
public class ShowSystemBetAddingPageCommand implements Command {

    private static final String SYSTEM_BET_ADDING_MESSAGE_KEY = "bet.system.adding";

    private static volatile ShowSystemBetAddingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betCommandResponse;

    private ShowSystemBetAddingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowSystemBetAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowSystemBetAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowSystemBetAddingPageCommand();
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

        request.setAttribute(SYSTEM_BET_ATTRIBUTE_NAME, messageManager.getString(SYSTEM_BET_ADDING_MESSAGE_KEY));
        request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);

        return betCommandResponse;
    }

}