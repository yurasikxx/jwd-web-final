package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowBetListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;

public class BetAddingCommand implements Command {

    private static final String BETSLIP_PARAMETER_NAME = "betslipId";
    private static final String BET_TOTAL_PARAMETER_NAME = "betTotal";
    private static final String PERSON_PARAMETER_NAME = "personId";
    private static final String BET_SUCCESSFULLY_ADDED_MSG = "Bet successfully added";
    private static volatile BetAddingCommand instance;

    private final PersonBaseService personService;
    private final BetslipBaseService betslipService;
    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse;
    private final BaseCommandResponse betErrorCommandResponse;

    private BetAddingCommand() {
        this.personService = PersonService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betService = BetService.getInstance();
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.betErrorCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static BetAddingCommand getInstance() {
        if (instance == null) {
            synchronized (BetAddingCommand.class) {
                if (instance == null) {
                    instance = new BetAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedBetslipId(request) == null
                    || getCheckedBetTotal(request) == null
                    || getCheckedPersonId(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            final Long betslipId = getCheckedBetslipId(request);
            final Integer betTotal = getCheckedBetTotal(request);
            final Long personId = getCheckedPersonId(request);

            final Bet bet = new Bet(betslipService.findById(betslipId), betTotal, personService.findById(personId));

            betService.save(bet);
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betErrorCommandResponse;
        }

        request.setAttribute(BET_ATTRIBUTE_NAME, BET_SUCCESSFULLY_ADDED_MSG);

        return betCommandResponse;
    }

    private Long getCheckedBetslipId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(BETSLIP_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(BETSLIP_PARAMETER_NAME));
            return id;
        }

        return null;
    }

    private Integer getCheckedBetTotal(BaseCommandRequest request) {
        final int betTotal;

        if (request.getParameter(BET_TOTAL_PARAMETER_NAME) != null) {
            betTotal = Integer.parseInt(request.getParameter(BET_TOTAL_PARAMETER_NAME));
            return betTotal;
        }

        return null;
    }

    private Long getCheckedPersonId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(PERSON_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(PERSON_PARAMETER_NAME));
            return id;
        }

        return null;
    }

}