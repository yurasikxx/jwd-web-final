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

import java.util.Objects;

import static com.epam.jwd.command.BetAddingCommand.BETSLIP_DOES_NOT_EXIST_MSG;
import static com.epam.jwd.command.BetAddingCommand.BETSLIP_PARAMETER_NAME;
import static com.epam.jwd.command.BetAddingCommand.BET_TOTAL_PARAMETER_NAME;
import static com.epam.jwd.command.BetAddingCommand.PERSON_DOES_NOT_EXIST_MSG;
import static com.epam.jwd.command.BetAddingCommand.PERSON_PARAMETER_NAME;
import static com.epam.jwd.command.BetslipAddingCommand.INCORRECT_ENTERED_DATA;
import static com.epam.jwd.command.BetslipAddingCommand.NUMBERS_MUST_BE_POSITIVE_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.MIN_ELEMENT_ID;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowBetAddingPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetAddingPageCommand.BET_TOTAL_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_BETSLIP_ID_MSG;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_BET_TOTAL_MSG;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_PERSON_ID_MSG;
import static com.epam.jwd.command.ShowBetAddingPageCommand.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetChangingPageCommand.BET_CHANGE_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetChangingPageCommand.BET_ID_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetChangingPageCommand.ENTER_ID_OF_CHANGEABLE_BET_MSG;
import static com.epam.jwd.command.ShowBetListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;

public class BetChangingCommand implements Command {

    private static final String WRONG_BET_ID_MSG = "Wrong bet ID";
    private static final String BET_SUCCESSFULLY_CHANGED_MSG = "Bet successfully changed";

    private static volatile BetChangingCommand instance;

    private final PersonBaseService personService;
    private final BetslipBaseService betslipService;
    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse;
    private final BaseCommandResponse betErrorCommandResponse;

    private BetChangingCommand() {
        this.personService = PersonService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betService = BetService.getInstance();
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.betErrorCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static BetChangingCommand getInstance() {
        if (instance == null) {
            synchronized (BetChangingCommand.class) {
                if (instance == null) {
                    instance = new BetChangingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedId(request) == null
                    || getCheckedBetslipId(request) == null
                    || getCheckedBetTotal(request) == null
                    || getCheckedPersonId(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedBetslipId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedBetTotal(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedPersonId(request)) < MIN_ELEMENT_ID) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, NUMBERS_MUST_BE_POSITIVE_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedBetslipId(request)) > betslipService.findAll().size()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, BETSLIP_DOES_NOT_EXIST_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedId(request)) > betService.findAll().size()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, WRONG_BET_ID_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedPersonId(request)) > personService.findAll().size()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, PERSON_DOES_NOT_EXIST_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            final Long id = getCheckedId(request);
            final Long betslipId = getCheckedBetslipId(request);
            final Integer betTotal = getCheckedBetTotal(request);
            final Long personId = getCheckedPersonId(request);

            final Bet bet = new Bet(id, betslipService.findById(betslipId), betTotal, personService.findById(personId));

            betService.update(bet);
        } catch (DaoException | ServiceException e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, INCORRECT_ENTERED_DATA);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betErrorCommandResponse;
        }

        request.setAttribute(BET_CHANGE_ATTRIBUTE_NAME, BET_SUCCESSFULLY_CHANGED_MSG);
        request.setAttribute(BET_ID_ATTRIBUTE_NAME, ENTER_ID_OF_CHANGEABLE_BET_MSG);
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, ENTER_BETSLIP_ID_MSG);
        request.setAttribute(BET_TOTAL_ATTRIBUTE_NAME, ENTER_BET_TOTAL_MSG);
        request.setAttribute(PERSON_ATTRIBUTE_NAME, ENTER_PERSON_ID_MSG);

        return betCommandResponse;
    }

    private Long getCheckedId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        return null;
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