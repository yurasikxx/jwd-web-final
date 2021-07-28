package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.model.BetResult;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.CompetitionResult;
import com.epam.jwd.model.Team;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetHistoryBaseService;
import com.epam.jwd.service.BetHistoryService;
import com.epam.jwd.service.BetService;

import java.util.Objects;

import static com.epam.jwd.command.CompetitionAddingCommand.MIN_ELEMENT_ID;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowAllBetsListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetCalculatingPageCommand.BET_HISTORY_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetCalculatingPageCommand.CALCULATING_JSP_PATH;
import static com.epam.jwd.command.ShowBetCalculatingPageCommand.ENTER_BET_ID_MSG;
import static com.epam.jwd.model.BetResult.LOSS;
import static com.epam.jwd.model.BetResult.WIN;

public class BetCalculateCommand implements Command {

    private static final String SUCCESSFUL_OPERATION_MESSAGE = "Bet successfully calculated and added to bet history";
    private static final String FIELD_MUST_BE_FILLED_MSG = "Bet ID field must be filled";
    private static final String NUMBER_MUST_BE_POSITIVE_MSG = "Entered number must be positive";
    private static final String BET_DOES_NOT_EXIST_MSG = "Bet with such ID doesn't exist";
    private static final String RESULT_WAS_NOT_FOUND_MSG = "Result wasn't found: %s";
    private static final String FIELD_MUST_BE_NUMBER_MSG = "Field must be number";
    private static final long RANDOM_COMPETITION_RESULT_ID = 1 + (long) (Math.random() * 3);

    private static volatile BetCalculateCommand instance;

    private final BetBaseService betService;
    private final BetHistoryBaseService betHistoryService;
    private final BaseCommandResponse betHistoryCommandResponse;
    private final BaseCommandResponse betHistoryErrorCommandResponse;

    private BetCalculateCommand() {
        this.betService = BetService.getInstance();
        this.betHistoryService = BetHistoryService.getInstance();
        this.betHistoryCommandResponse = new CommandResponse(CALCULATING_JSP_PATH, false);
        this.betHistoryErrorCommandResponse = betHistoryCommandResponse;
    }

    public static BetCalculateCommand getInstance() {
        if (instance == null) {
            synchronized (BetCalculateCommand.class) {
                if (instance == null) {
                    instance = new BetCalculateCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedBetId(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, FIELD_MUST_BE_FILLED_MSG);
                request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betHistoryErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedBetId(request)) < MIN_ELEMENT_ID) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, NUMBER_MUST_BE_POSITIVE_MSG);
                request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betHistoryErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedBetId(request)) > betService.findAll().size()) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, BET_DOES_NOT_EXIST_MSG);
                request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betHistoryErrorCommandResponse;
            }

            final Long betId = getCheckedBetId(request);
            final Bet bet = betService.findById(betId);
            final Team home = bet.getBetslip().getCompetition().getHome();
            final Team away = bet.getBetslip().getCompetition().getAway();
            final BetType betType = bet.getBetslip().getBetType();
            final Double coefficient = bet.getBetslip().getCoefficient();
            final Integer betTotal = bet.getBetTotal();
            final String personLogin = bet.getPerson().getLogin();
            final CompetitionResult competitionResult = CompetitionResult
                    .resolveCompetitionResultById(RANDOM_COMPETITION_RESULT_ID);
            final BetResult betResult;

            switch (competitionResult) {
                case HOME_WIN:
                    if (betType.getId().equals(BetType.HOME_TEAM_WIN.getId())
                            || betType.getId().equals(BetType.HOME_TEAM_WILL_NOT_LOSE.getId())
                            || betType.getId().equals(BetType.NO_DRAW.getId())) {
                        betResult = WIN;
                    } else {
                        betResult = LOSS;
                    }
                    break;
                case AWAY_TEAM:
                    if (betType.getId().equals(BetType.AWAY_TEAM_WIN.getId())
                            || betType.getId().equals(BetType.AWAY_TEAM_WILL_NOT_LOSE.getId())
                            || betType.getId().equals(BetType.NO_DRAW.getId())) {
                        betResult = WIN;
                    } else {
                        betResult = LOSS;
                    }
                    break;
                case DRAW:
                    if (betType.getId().equals(BetType.DRAW.getId())
                            || betType.getId().equals(BetType.HOME_TEAM_WILL_NOT_LOSE.getId())
                            || betType.getId().equals(BetType.AWAY_TEAM_WILL_NOT_LOSE.getId())) {
                        betResult = WIN;
                    } else {
                        betResult = LOSS;
                    }
                    break;
                default:
                    throw new IllegalAccessException(String.format(RESULT_WAS_NOT_FOUND_MSG, competitionResult.getName()));
            }

            final BetHistory betHistory = new BetHistory(home, away, betType, coefficient, betTotal,
                    personLogin, competitionResult, betResult);

            betHistoryService.save(betHistory);
        } catch (DaoException | ServiceException e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betHistoryErrorCommandResponse;
        } catch (IllegalAccessException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, RESULT_WAS_NOT_FOUND_MSG);
            request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betHistoryErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, FIELD_MUST_BE_NUMBER_MSG);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betHistoryErrorCommandResponse;
        }

        request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, SUCCESSFUL_OPERATION_MESSAGE);
        request.setAttribute(BET_ATTRIBUTE_NAME, ENTER_BET_ID_MSG);

        return betHistoryCommandResponse;
    }

    private Long getCheckedBetId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        return null;
    }

}