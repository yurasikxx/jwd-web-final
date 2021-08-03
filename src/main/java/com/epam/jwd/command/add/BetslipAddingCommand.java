package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.constant.Constant.BETSLIP_ALREADY_EXISTS_MSG;
import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.BET_TYPE_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.COEFFICIENT_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.COMPETITION_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.NUMBERS_MUST_BE_POSITIVE_MSG;
import static com.epam.jwd.constant.Constant.SELECT_BET_TYPE_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class BetslipAddingCommand implements Command {

    private static final String BETSLIP_SUCCESSFULLY_ADDED_MSG = "Betslip successfully added";
    private static final int INITIAL_RANDOM_NUMBER_VALUE = 2;
    private static final int RANDOM_NUMBER_RANGE = 8;
    private static final String COMPETITION_OR_BET_TYPE_NOT_SELECTED_MSG = "Competition or bet type not selected";

    private static volatile BetslipAddingCommand instance;

    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse;

    private BetslipAddingCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betslipCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
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
        return getCommandResponse(request);
    }

    private BaseCommandResponse getCommandResponse(BaseCommandRequest request) {
        try {
            final Long competitionId = getCheckedCompetitionId(request);
            final Long betTypeId = getCheckedBetTypeId(request);
            final int coefficient;

            if (Objects.requireNonNull(getCheckedCoefficient(request)) == MIN_INDEX_VALUE) {
                coefficient = (int) (INITIAL_RANDOM_NUMBER_VALUE + Math.random() * RANDOM_NUMBER_RANGE);
            } else {
                coefficient = getCheckedCoefficient(request);
            }

            if (competitionId < MIN_LONG_ID_VALUE || betTypeId < MIN_LONG_ID_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, COMPETITION_OR_BET_TYPE_NOT_SELECTED_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipCommandResponse;
            }

            if (coefficient < MIN_INDEX_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, NUMBERS_MUST_BE_POSITIVE_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipCommandResponse;
            }

            final Betslip betslip = new Betslip(competitionService.findById(competitionId),
                    BetType.resolveBetTypeById(betTypeId), coefficient);

            if (!betslipService.canSave(betslip)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, BETSLIP_ALREADY_EXISTS_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipCommandResponse;
            }

            betslipService.save(betslip);

            final List<Competition> competitions = competitionService.findAll()
                    .stream()
                    .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                    .collect(Collectors.toList());
            final List<BetType> betTypes = Arrays.stream(BetType.values()).collect(Collectors.toList());

            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_SUCCESSFULLY_ADDED_MSG);
            request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);
            request.setAttribute(SELECT_BET_TYPE_ATTRIBUTE_NAME, betTypes);

            return betslipCommandResponse;
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipCommandResponse;
        } catch (DaoException | ServiceException | UnknownEnumAttributeException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipCommandResponse;
        }
    }

    private Long getCheckedCompetitionId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(COMPETITION_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(COMPETITION_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

    private Long getCheckedBetTypeId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(BET_TYPE_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(BET_TYPE_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

    private Integer getCheckedCoefficient(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final int coefficient;

        if (request.getParameter(COEFFICIENT_PARAMETER_NAME) != null) {
            coefficient = Integer.parseInt(request.getParameter(COEFFICIENT_PARAMETER_NAME));
            return coefficient;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

}