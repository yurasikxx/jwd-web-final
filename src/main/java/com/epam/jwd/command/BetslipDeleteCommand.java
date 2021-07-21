package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;

import java.util.List;

import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class BetslipDeleteCommand implements Command {

    private static volatile BetslipDeleteCommand instance;

    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private BetslipDeleteCommand() {
        this.betslipService = BetslipService.getInstance();
    }

    public static BetslipDeleteCommand getInstance() {
        if (instance == null) {
            synchronized (BetslipDeleteCommand.class) {
                if (instance == null) {
                    instance = new BetslipDeleteCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll();
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, betslips);
        final String id = request.getParameter(ID_PARAMETER_NAME);

        try {
            betslipService.delete(Long.parseLong(id));
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
        }

        return betslipCommandResponse;
    }

}