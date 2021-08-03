package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_BET_TYPE_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;

public class ShowBetslipAddingPageCommand implements Command {

    private static final String BETSLIP_ADDING_OPERATION_MSG = "Betslip adding operation";

    private static volatile ShowBetslipAddingPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse betslipCommandResponse;

    private ShowBetslipAddingPageCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.betslipCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowBetslipAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Competition> competitions = competitionService.findAll()
                .stream()
                .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());
        final List<BetType> betTypes = Arrays.stream(BetType.values()).collect(Collectors.toList());

        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_ADDING_OPERATION_MSG);
        request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);
        request.setAttribute(SELECT_BET_TYPE_ATTRIBUTE_NAME, betTypes);

        return betslipCommandResponse;
    }

}