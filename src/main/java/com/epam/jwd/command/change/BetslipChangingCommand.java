package com.epam.jwd.command.change;

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
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.BET_TYPE_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COEFFICIENT_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.COMPETITION_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.NUMBERS_POSITIVE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_BET_TYPE_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

public class BetslipChangingCommand implements Command {

    private static final String BETSLIP_CHANGED_MESSAGE_KEY = "betslip.changed";
    private static final String BETSLIP_COMPETITION_BET_TYPE_SELECT_MESSAGE_KEY = "betslip.competition.bet.type.select";

    private static volatile BetslipChangingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse betslipCommandResponse;

    private BetslipChangingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betslipCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static BetslipChangingCommand getInstance() {
        if (instance == null) {
            synchronized (BetslipChangingCommand.class) {
                if (instance == null) {
                    instance = new BetslipChangingCommand();
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
            final Long id = getCheckedId(request);
            final Long competitionId = getCheckedCompetitionId(request);
            final Long betTypeId = getCheckedBetTypeId(request);
            final Integer coefficient = getCheckedCoefficient(request);

            if (id < MIN_LONG_ID_VALUE || competitionId < MIN_LONG_ID_VALUE || betTypeId < MIN_LONG_ID_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_COMPETITION_BET_TYPE_SELECT_MESSAGE_KEY));
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return betslipCommandResponse;
            }

            if (coefficient < MIN_LONG_ID_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(NUMBERS_POSITIVE_MESSAGE_KEY));
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return betslipCommandResponse;
            }

            final Betslip betslip = new Betslip(id, competitionService.findById(competitionId),
                    BetType.resolveBetTypeById(betTypeId), coefficient);

            betslipService.update(betslip);

            final List<Betslip> betslips = betslipService.findAll();
            final List<Competition> competitions = competitionService.findAll();
            final List<BetType> betTypes = Arrays.stream(BetType.values()).collect(Collectors.toList());

            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_CHANGED_MESSAGE_KEY));
            request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);
            request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);
            request.setAttribute(SELECT_BET_TYPE_ATTRIBUTE_NAME, betTypes);
        } catch (DaoException | ServiceException | UnknownEnumAttributeException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        }

        return betslipCommandResponse;
    }

    private Long getCheckedId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
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