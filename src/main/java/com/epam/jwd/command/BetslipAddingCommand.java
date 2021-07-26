package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Objects;

import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.MIN_ELEMENT_ID;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.BET_TYPE_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.COEFFICIENT_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.ENTER_BET_TYPE_ID_MSG;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.ENTER_COEFFICIENT_MSG;
import static com.epam.jwd.command.ShowBetslipAddingPageCommand.ENTER_COMPETITION_ID_MSG;
import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;
import static java.lang.Math.ceil;
import static java.lang.Math.pow;

public class BetslipAddingCommand implements Command {

    protected static final String NUMBERS_MUST_BE_POSITIVE_MSG = "Numbers must be positive";
    protected static final String INCORRECT_ENTERED_DATA = ALL_FIELDS_MUST_BE_FILLED_MSG + " and must be numbers";
    protected static final String COMPETITION_PARAMETER_NAME = "competitionId";
    protected static final String BET_TYPE_PARAMETER_NAME = "betTypeId";
    protected static final String COEFFICIENT_PARAMETER_NAME = "coefficient";
    protected static final String UNKNOWN_BET_TYPE_ID_MSG = "Unknown bet type ID";
    protected static final String WRONG_COMPETITION_ID_MSG = "Competition with such ID doesn't exist";
    protected static final String BETSLIP_ALREADY_EXISTS_MSG = "Betslip of given competition with such bet type already exists";

    private static final String BETSLIP_SUCCESSFULLY_ADDED_MSG = "Betslip successfully added";
    private static final int ZERO_ELEMENT_ID = 0;
    private static final int INITIAL_RANDOM_NUMBER_VALUE = MIN_ELEMENT_ID.intValue();
    private static final int RANDOM_NUMBER_RANGE = 10;
    private static final int BASE = 10;
    private static final int EXPONENT = 2;

    private static volatile BetslipAddingCommand instance;

    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse;
    private final BaseCommandResponse betslipErrorCommandResponse;

    private BetslipAddingCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betslipCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.betslipErrorCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static BetslipAddingCommand getInstance() {
        if (instance == null) {
            synchronized (BetslipAddingCommand.class) {
                if (instance == null) {
                    instance = new BetslipAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedCompetitionId(request) == null
                    || getCheckedBetTypeId(request) == null
                    || getCheckedCoefficient(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedCompetitionId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedBetTypeId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedCoefficient(request)) < ZERO_ELEMENT_ID) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, NUMBERS_MUST_BE_POSITIVE_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedBetTypeId(request)) > BetType.values().length) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, UNKNOWN_BET_TYPE_ID_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedCompetitionId(request)) > competitionService.findAll().size()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, WRONG_COMPETITION_ID_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipErrorCommandResponse;
            }

            final Long competitionId = getCheckedCompetitionId(request);
            final Long betTypeId = getCheckedBetTypeId(request);
            final Double coefficient;

            if (Objects.requireNonNull(getCheckedCoefficient(request)) == ZERO_ELEMENT_ID) {
                final double number = INITIAL_RANDOM_NUMBER_VALUE + Math.random() * RANDOM_NUMBER_RANGE;
                final double scale = pow(BASE, EXPONENT);
                coefficient = ceil(number * scale) / scale;
            } else {
                coefficient = getCheckedCoefficient(request);
            }

            final Betslip betslip = new Betslip(competitionService.findById(competitionId),
                    BetType.resolveBetTypeById(betTypeId), coefficient);

            if (!betslipService.canSave(betslip)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, BETSLIP_ALREADY_EXISTS_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipErrorCommandResponse;
            }

            betslipService.save(betslip);
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, INCORRECT_ENTERED_DATA);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipErrorCommandResponse;
        }

        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_SUCCESSFULLY_ADDED_MSG);
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, ENTER_COMPETITION_ID_MSG);
        request.setAttribute(BET_TYPE_ATTRIBUTE_NAME, ENTER_BET_TYPE_ID_MSG);
        request.setAttribute(COEFFICIENT_ATTRIBUTE_NAME, ENTER_COEFFICIENT_MSG);

        return betslipCommandResponse;
    }

    private Long getCheckedCompetitionId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(COMPETITION_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(COMPETITION_PARAMETER_NAME));
            return id;
        }

        return null;
    }

    private Long getCheckedBetTypeId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(BET_TYPE_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(BET_TYPE_PARAMETER_NAME));
            return id;
        }

        return null;
    }

    private Double getCheckedCoefficient(BaseCommandRequest request) {
        final double coefficient;

        if (request.getParameter(COEFFICIENT_PARAMETER_NAME) != null) {
            coefficient = Double.parseDouble(request.getParameter(COEFFICIENT_PARAMETER_NAME));
            return coefficient;
        }

        return null;
    }

}