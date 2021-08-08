package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Objects;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.BETSLIP_ALREADY_EXISTS_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.BETSLIP_JSP_PATH;
import static com.epam.jwd.constant.Constant.BET_TYPE_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.COEFFICIENT_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.COMPETITION_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.NUMBERS_POSITIVE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code BetslipAddingCommand} class implements {@code Command}
 * interface and execute command that adds betslip.
 *
 * @see Command
 */
public class BetslipAddingCommand implements Command {

    private static final String COMPETITION_BET_TYPE_SELECT_MESSAGE_KEY = "competition.bet.type.select";
    private static final int INITIAL_RANDOM_NUMBER_VALUE = 2;
    private static final int RANDOM_NUMBER_RANGE = 8;

    private static volatile BetslipAddingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse successAddingCommandResponse;
    private final BaseCommandResponse errorAddingCommandResponse;

    private BetslipAddingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.successAddingCommandResponse = new CommandResponse(BETSLIP_JSP_PATH, true);
        this.errorAddingCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
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
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_BET_TYPE_SELECT_MESSAGE_KEY));
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return errorAddingCommandResponse;
            }

            if (coefficient < MIN_INDEX_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(NUMBERS_POSITIVE_MESSAGE_KEY));
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return errorAddingCommandResponse;
            }

            final Betslip betslip = new Betslip(competitionService.findById(competitionId),
                    BetType.resolveBetTypeById(betTypeId), coefficient);

            if (!betslipService.canSave(betslip)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_ALREADY_EXISTS_MESSAGE_KEY));
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return errorAddingCommandResponse;
            }

            betslipService.save(betslip);
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        } catch (DaoException | ServiceException | UnknownEnumAttributeException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        }

        return successAddingCommandResponse;
    }

    private Long getCheckedCompetitionId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(COMPETITION_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(COMPETITION_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private Long getCheckedBetTypeId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(BET_TYPE_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(BET_TYPE_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private Integer getCheckedCoefficient(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final int coefficient;

        if (request.getParameter(COEFFICIENT_PARAMETER_NAME) != null) {
            coefficient = Integer.parseInt(request.getParameter(COEFFICIENT_PARAMETER_NAME));
            return coefficient;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

}