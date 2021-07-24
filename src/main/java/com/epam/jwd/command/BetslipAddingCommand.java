package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowBetslipListPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;

public class BetslipAddingCommand implements Command {

    private static final String COMPETITION_PARAMETER_NAME = "competitionId";
    private static final String BET_TYPE_PARAMETER_NAME = "betTypeId";
    private static final String COEFFICIENT_PARAMETER_NAME = "coefficient";
    private static final String BETSLIP_SUCCESSFULLY_ADDED_MSG = "Betslip successfully added";

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
                request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betslipErrorCommandResponse;
            }

            final Long competitionId = getCheckedCompetitionId(request);
            final Long betTypeId = getCheckedBetTypeId(request);
            final Double coefficient = getCheckedCoefficient(request);

            final Betslip betslip = new Betslip(competitionService.findById(competitionId),
                    BetType.resolveBetTypeById(betTypeId), coefficient);

            betslipService.save(betslip);
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betslipErrorCommandResponse;
        }

        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, BETSLIP_SUCCESSFULLY_ADDED_MSG);

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