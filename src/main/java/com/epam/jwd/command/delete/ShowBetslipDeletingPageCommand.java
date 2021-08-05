package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;

public class ShowBetslipDeletingPageCommand implements Command {

    private static final String BETSLIP_DELETING_MESSAGE_KEY = "betslip.deleting";

    private static volatile ShowBetslipDeletingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipDeletingCommandResponse;

    private ShowBetslipDeletingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
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
        final List<Betslip> betslips = betslipService.findAll()
                .stream()
                .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());

        request.setAttribute(BETSLIP_ATTRIBUTE_NAME,
                messageManager.getString(BETSLIP_DELETING_MESSAGE_KEY));
        request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);

        return betslipDeletingCommandResponse;
    }

}