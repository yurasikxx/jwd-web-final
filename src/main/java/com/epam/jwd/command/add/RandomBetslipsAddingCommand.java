package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.BETSLIP_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;

public class RandomBetslipsAddingCommand implements Command {

    private static final String BETSLIP_PRESENCE_MESSAGE_KEY = "betslip.presence";
    private static final int INITIAL_RANDOM_NUMBER_VALUE = 2;
    private static final int RANDOM_NUMBER_RANGE = 8;

    private static volatile RandomBetslipsAddingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BetslipBaseService betslipService;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse successAddingCommandResponse;
    private final BaseCommandResponse errorAddingCommandResponse;

    private RandomBetslipsAddingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.successAddingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorAddingCommandResponse = new CommandResponse(BETSLIP_JSP_PATH, false);
    }

    public static RandomBetslipsAddingCommand getInstance() {
        if (instance == null) {
            synchronized (RandomBetslipsAddingCommand.class) {
                if (instance == null) {
                    instance = new RandomBetslipsAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Betslip> betslips = betslipService.findAll();
        final List<Competition> competitions = competitionService.findAll();
        final int betslipTypeSize = BetslipType.values().length;

        if (checkBetslipPresence(request, betslips)) {
            return errorAddingCommandResponse;
        }

        if (createBetslips(request, competitions, betslipTypeSize)) {
            return errorAddingCommandResponse;
        }

        return successAddingCommandResponse;
    }

    private boolean createBetslips(BaseCommandRequest request, List<Competition> competitions, int betslipTypeSize) {
        for (int i = 0; i < competitions.size(); i++) {
            final Competition competition = competitionService.findById(i + MIN_LONG_ID_VALUE);

            for (int j = 0; j < betslipTypeSize; j++) {
                try {
                    final BetslipType betslipType = BetslipType.resolveBetslipTypeById(j + MIN_LONG_ID_VALUE);
                    final int coefficient = (int) (INITIAL_RANDOM_NUMBER_VALUE + Math.random() * RANDOM_NUMBER_RANGE);
                    final Betslip betslip = new Betslip(competition, betslipType, coefficient);

                    betslipService.save(betslip);
                } catch (ServiceException | UnknownEnumAttributeException e) {
                    request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBetslipPresence(BaseCommandRequest request, List<Betslip> betslips) {
        if (!betslips.isEmpty()) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_PRESENCE_MESSAGE_KEY));
            return true;
        }
        return false;
    }
}