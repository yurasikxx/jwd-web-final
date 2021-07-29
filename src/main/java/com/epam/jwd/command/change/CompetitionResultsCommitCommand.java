package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.model.BetResult;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.CompetitionResult;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Team;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetHistoryBaseService;
import com.epam.jwd.service.BetHistoryService;
import com.epam.jwd.service.BetService;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.BET_HISTORY_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;
import static com.epam.jwd.model.BetResult.LOSS;
import static com.epam.jwd.model.BetResult.WIN;
import static com.epam.jwd.model.BetType.AWAY_TEAM_WILL_NOT_LOSE;
import static com.epam.jwd.model.BetType.AWAY_TEAM_WIN;
import static com.epam.jwd.model.BetType.DRAW;
import static com.epam.jwd.model.BetType.HOME_TEAM_WILL_NOT_LOSE;
import static com.epam.jwd.model.BetType.HOME_TEAM_WIN;
import static com.epam.jwd.model.BetType.NO_DRAW;

public class CompetitionResultsCommitCommand implements Command {

    private static final String SUCCESSFUL_OPERATION_MESSAGE = "Competition results successfully committed and " +
            "all related bets added to bet history";
    private static final String FIELD_MUST_BE_FILLED_MSG = "Competition ID field must be filled";
    private static final String NUMBER_MUST_BE_POSITIVE_MSG = "Entered number must be positive";
    private static final String COMPETITION_DOES_NOT_EXIST_MSG = "Competition with such ID doesn't exist";
    private static final String RESULT_WAS_NOT_FOUND_MSG = "Result wasn't found: %s";
    private static final String FIELD_MUST_BE_NUMBER_MSG = "Field must be number";
    private static final long RANDOM_COMPETITION_RESULT_ID = 1 + (long) (Math.random() * 3);

    private static volatile CompetitionResultsCommitCommand instance;

    private final PersonBaseService personService;
    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BetBaseService betService;
    private final BetHistoryBaseService betHistoryService;
    private final BaseCommandResponse betHistoryCommandResponse;
    private final BaseCommandResponse betHistoryErrorCommandResponse;

    private CompetitionResultsCommitCommand() {
        this.personService = PersonService.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betService = BetService.getInstance();
        this.betHistoryService = BetHistoryService.getInstance();
        this.betHistoryCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
        this.betHistoryErrorCommandResponse = betHistoryCommandResponse;
    }

    public static CompetitionResultsCommitCommand getInstance() {
        if (instance == null) {
            synchronized (CompetitionResultsCommitCommand.class) {
                if (instance == null) {
                    instance = new CompetitionResultsCommitCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedCompetitionId(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, FIELD_MUST_BE_FILLED_MSG);
                request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betHistoryErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedCompetitionId(request)) < MIN_LONG_ID_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, NUMBER_MUST_BE_POSITIVE_MSG);
                request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betHistoryErrorCommandResponse;
            }

            if (!competitionService.findAll()
                    .contains(competitionService.findById(Objects.requireNonNull(getCheckedCompetitionId(request))))) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, COMPETITION_DOES_NOT_EXIST_MSG);
                request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betHistoryErrorCommandResponse;
            }

            final Long competitionId = getCheckedCompetitionId(request);
            final List<Betslip> betslips = betslipService.findByCompetitionId(competitionId);
            final List<Bet> bets = betService.findByCompetitionId(competitionId);
            final List<BetHistory> historyBets = new ArrayList<>();
            final List<Person> winBetPersons = new ArrayList<>();

            for (Bet bet : bets) {
                final Team home = bet.getBetslip().getCompetition().getHome();
                final Team away = bet.getBetslip().getCompetition().getAway();
                final BetType betType = bet.getBetslip().getBetType();
                final Double coefficient = bet.getBetslip().getCoefficient();
                final Integer betTotal = bet.getBetTotal();
                final String personLogin = bet.getPerson().getLogin();
                final CompetitionResult competitionResult = CompetitionResult
                        .resolveCompetitionResultById(RANDOM_COMPETITION_RESULT_ID);
                final BetResult betResult = getBetResult(betType, competitionResult);
                historyBets.add(new BetHistory(home, away, betType, coefficient, betTotal,
                        personLogin, competitionResult, betResult));
            }

            for (BetHistory historyBet : historyBets) {
                betHistoryService.save(historyBet);
            }

            for (BetHistory historyBet : historyBets) {
                if (historyBet.getBetResult().equals(WIN)) {
                    final Person person = personService.findByLogin(historyBet.getPersonLogin());
                    winBetPersons.add(new Person(person.getId(), person.getLogin(), person.getPassword(),
                            person.getBalance() + historyBet.getBetTotal() * historyBet.getCoefficient(),
                            person.getRole()));
                }
            }

            for (Person winBetPerson : winBetPersons) {
                personService.updateBalance(winBetPerson);
            }

            for (Bet bet : bets) {
                betService.delete(bet.getId());
            }

            for (Betslip betslip : betslips) {
                betslipService.delete(betslip.getId());
            }

            competitionService.delete(competitionId);
        } catch (DaoException | ServiceException | UnknownEnumAttributeException e) {
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

        return betHistoryCommandResponse;
    }

    private BetResult getBetResult(BetType betType, CompetitionResult competitionResult) throws IllegalAccessException {
        final BetResult betResult;
        switch (competitionResult) {
            case HOME_WIN:
                if (betType.getId().equals(HOME_TEAM_WIN.getId())
                        || betType.getId().equals(HOME_TEAM_WILL_NOT_LOSE.getId())
                        || betType.getId().equals(NO_DRAW.getId())) {
                    betResult = WIN;
                } else {
                    betResult = LOSS;
                }
                break;
            case AWAY_TEAM:
                if (betType.getId().equals(AWAY_TEAM_WIN.getId())
                        || betType.getId().equals(AWAY_TEAM_WILL_NOT_LOSE.getId())
                        || betType.getId().equals(NO_DRAW.getId())) {
                    betResult = WIN;
                } else {
                    betResult = LOSS;
                }
                break;
            case DRAW:
                if (betType.getId().equals(DRAW.getId())
                        || betType.getId().equals(HOME_TEAM_WILL_NOT_LOSE.getId())
                        || betType.getId().equals(AWAY_TEAM_WILL_NOT_LOSE.getId())) {
                    betResult = WIN;
                } else {
                    betResult = LOSS;
                }
                break;
            default:
                throw new IllegalAccessException(String.format(RESULT_WAS_NOT_FOUND_MSG, competitionResult.getName()));
        }
        return betResult;
    }

    private Long getCheckedCompetitionId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        return null;
    }

}