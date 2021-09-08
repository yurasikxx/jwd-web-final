package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.model.BetResult;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;
import com.epam.jwd.model.Competition;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.BET_HISTORY_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.EMPTY_LIST_SIZE_VALUE;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;
import static com.epam.jwd.model.BetResult.LOSS;
import static com.epam.jwd.model.BetResult.WIN;
import static com.epam.jwd.model.BetType.PARLAY;
import static com.epam.jwd.model.BetType.SINGLE;
import static com.epam.jwd.model.BetType.SYSTEM;
import static com.epam.jwd.model.BetslipType.AWAY_TEAM_WILL_NOT_LOSE;
import static com.epam.jwd.model.BetslipType.AWAY_TEAM_WIN;
import static com.epam.jwd.model.BetslipType.DRAW;
import static com.epam.jwd.model.BetslipType.HOME_TEAM_WILL_NOT_LOSE;
import static com.epam.jwd.model.BetslipType.HOME_TEAM_WIN;
import static com.epam.jwd.model.BetslipType.NO_DRAW;

/**
 * A {@code CompetitionResultsCommitCommand} class implements {@code Command}
 * interface and execute command that commits competition results.
 *
 * @see Command
 */
public class CompetitionResultsCommitCommand implements Command {

    private static final String RESULT_WAS_NOT_FOUND_MSG = "Result wasn't found: %s";
    private static final int MIN_RANDOM_VALUE = 1;
    private static final int MAX_RANDOM_VALUE = 3;
    private static final int SYSTEM_GROUP_HALF_DENOMINATOR = 2;

    private static volatile CompetitionResultsCommitCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BetBaseService betService;
    private final BetHistoryBaseService betHistoryService;
    private final BaseCommandResponse successCommittingCommandResponse;
    private final BaseCommandResponse errorCommittingCommandResponse;

    private CompetitionResultsCommitCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betService = BetService.getInstance();
        this.betHistoryService = BetHistoryService.getInstance();
        this.successCommittingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorCommittingCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
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
            final List<Bet> singleBets = betService.findByBetType(SINGLE);
            final List<Bet> parlayBets = betService.findByBetType(PARLAY);
            final List<Bet> systemBets = betService.findByBetType(SYSTEM);
            final List<BetHistory> singleHistoryBets = createHistoryBet(singleBets, SINGLE);
            final List<BetHistory> parlayHistoryBets = createHistoryBet(parlayBets, PARLAY);
            final List<BetHistory> systemHistoryBets = createHistoryBet(systemBets, SYSTEM);

            saveHistoryBet(singleHistoryBets);
            saveHistoryBet(parlayHistoryBets);
            saveHistoryBet(systemHistoryBets);

            updateSingleBetWin(singleHistoryBets);
            updateParlayBetWin(parlayHistoryBets);
            updateSystemBetWin(systemHistoryBets);

            deleteBetRelatedInfo();
        } catch (DaoException | ServiceException | UnknownEnumAttributeException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorCommittingCommandResponse;
        }

        return successCommittingCommandResponse;
    }

    private List<BetHistory> createHistoryBet(List<Bet> bets, BetType betType) throws UnknownEnumAttributeException {
        final List<BetHistory> historyBets = new ArrayList<>();

        for (Bet bet : bets) {
            final Team home = bet.getBetslip().getCompetition().getHome();
            final Team away = bet.getBetslip().getCompetition().getAway();
            final BetslipType betslipType = bet.getBetslip().getBetslipType();
            final Integer coefficient = bet.getBetslip().getCoefficient();
            final Integer betTotal = bet.getBetTotal();
            final String personLogin = bet.getPerson().getLogin();
            final CompetitionResult competitionResult = CompetitionResult
                    .resolveCompetitionResultById(MIN_RANDOM_VALUE + (long) (Math.random() * MAX_RANDOM_VALUE));
            final BetResult betResult = getBetResult(betslipType, competitionResult);
            final BetHistory betHistory = new BetHistory(home, away, betslipType, betType, coefficient, betTotal,
                    personLogin, competitionResult, betResult);

            historyBets.add(betHistory);
        }

        return historyBets;
    }

    private void saveHistoryBet(List<BetHistory> historyBets) throws ServiceException, DaoException {
        for (BetHistory historyBet : historyBets) {
            betHistoryService.save(historyBet);
        }
    }

    private BetResult getBetResult(BetslipType betslipType, CompetitionResult competitionResult) throws UnknownEnumAttributeException {
        final BetResult betResult;

        switch (competitionResult) {
            case HOME_WIN:
                if (betslipType.getId().equals(HOME_TEAM_WIN.getId())
                        || betslipType.getId().equals(HOME_TEAM_WILL_NOT_LOSE.getId())
                        || betslipType.getId().equals(NO_DRAW.getId())) {
                    betResult = WIN;
                } else {
                    betResult = LOSS;
                }
                break;
            case AWAY_TEAM:
                if (betslipType.getId().equals(AWAY_TEAM_WIN.getId())
                        || betslipType.getId().equals(AWAY_TEAM_WILL_NOT_LOSE.getId())
                        || betslipType.getId().equals(NO_DRAW.getId())) {
                    betResult = WIN;
                } else {
                    betResult = LOSS;
                }
                break;
            case DRAW:
                if (betslipType.getId().equals(DRAW.getId())
                        || betslipType.getId().equals(HOME_TEAM_WILL_NOT_LOSE.getId())
                        || betslipType.getId().equals(AWAY_TEAM_WILL_NOT_LOSE.getId())) {
                    betResult = WIN;
                } else {
                    betResult = LOSS;
                }
                break;
            default:
                throw new UnknownEnumAttributeException(String.format(RESULT_WAS_NOT_FOUND_MSG, competitionResult.getName()));
        }

        return betResult;
    }

    private void updateSingleBetWin(List<BetHistory> historyBets) throws ServiceException, DaoException {
        final List<Person> winBetPersons = new ArrayList<>();

        for (BetHistory historyBet : historyBets) {
            if (historyBet.getBetResult().equals(WIN)) {
                final Person person = personService.findByLogin(historyBet.getPersonLogin());
                final int newBalance = person.getBalance() + historyBet.getBetTotal() * historyBet.getCoefficient();
                final Person updatedPerson = new Person(person.getId(), person.getLogin(), person.getPassword(),
                        newBalance, person.getRole());

                winBetPersons.add(updatedPerson);
            }
        }

        for (Person winBetPerson : winBetPersons) {
            personService.updateBalance(winBetPerson);
        }
    }

    private void updateParlayBetWin(List<BetHistory> historyBets) throws ServiceException, DaoException {
        final List<List<BetHistory>> groupedByPersonHistoryBets = new ArrayList<>();
        List<BetHistory> group = new ArrayList<>();
        final List<Person> winBetPersons = new ArrayList<>();
        final List<BetHistory> sortedHistoryBets = historyBets
                .stream()
                .sorted(Comparator.comparing(BetHistory::getPersonLogin))
                .collect(Collectors.toList());

        for (int i = 0; i < sortedHistoryBets.size(); i++) {
            if (i != sortedHistoryBets.size() - INDEX_ROLLBACK_VALUE
                    && sortedHistoryBets.get(i).getPersonLogin()
                    .equals(sortedHistoryBets.get(i + INDEX_ROLLBACK_VALUE).getPersonLogin())) {
                group.add(sortedHistoryBets.get(i));
            } else {
                group.add(sortedHistoryBets.get(i));
                groupedByPersonHistoryBets.add(group);
                group = new ArrayList<>();
            }
        }

        for (List<BetHistory> groupedByPersonHistoryBet : groupedByPersonHistoryBets) {
            int parlayCoefficient = INITIAL_INDEX_VALUE;
            int winCounter = EMPTY_LIST_SIZE_VALUE;
            int winBetTotal = EMPTY_LIST_SIZE_VALUE;
            String winnerLogin = null;

            for (BetHistory historyBet : groupedByPersonHistoryBet) {
                if (historyBet.getBetResult().equals(WIN)) {
                    parlayCoefficient *= historyBet.getCoefficient();
                    winCounter++;
                }

                winBetTotal = historyBet.getBetTotal();
                winnerLogin = historyBet.getPersonLogin();
            }

            if (winCounter == groupedByPersonHistoryBet.size()) {
                final Person person = personService.findByLogin(winnerLogin);
                final int newBalance = person.getBalance() + winBetTotal * parlayCoefficient;
                final Person updatedPerson = new Person(person.getId(), person.getLogin(), person.getPassword(),
                        newBalance, person.getRole());

                winBetPersons.add(updatedPerson);
            }
        }

        for (Person winBetPerson : winBetPersons) {
            personService.updateBalance(winBetPerson);
        }
    }

    private void updateSystemBetWin(List<BetHistory> historyBets) throws ServiceException, DaoException {
        final List<List<BetHistory>> groupedByPersonHistoryBets = new ArrayList<>();
        List<BetHistory> group = new ArrayList<>();
        final List<Person> winBetPersons = new ArrayList<>();
        final List<BetHistory> sortedHistoryBets = historyBets
                .stream()
                .sorted(Comparator.comparing(BetHistory::getPersonLogin))
                .collect(Collectors.toList());

        for (int i = 0; i < sortedHistoryBets.size(); i++) {
            if (i != sortedHistoryBets.size() - INDEX_ROLLBACK_VALUE
                    && sortedHistoryBets.get(i).getPersonLogin()
                    .equals(sortedHistoryBets.get(i + INDEX_ROLLBACK_VALUE).getPersonLogin())) {
                group.add(sortedHistoryBets.get(i));
            } else {
                group.add(sortedHistoryBets.get(i));
                groupedByPersonHistoryBets.add(group);
                group = new ArrayList<>();
            }
        }

        for (List<BetHistory> groupedByPersonHistoryBet : groupedByPersonHistoryBets) {
            int parlayCoefficient = INITIAL_INDEX_VALUE;
            int winCounter = EMPTY_LIST_SIZE_VALUE;
            int winBetTotal = EMPTY_LIST_SIZE_VALUE;
            String winnerLogin = null;

            for (BetHistory historyBet : groupedByPersonHistoryBet) {
                if (historyBet.getBetResult().equals(WIN)) {
                    parlayCoefficient *= historyBet.getCoefficient();
                    winCounter++;
                }

                winBetTotal = historyBet.getBetTotal();
                winnerLogin = historyBet.getPersonLogin();
            }

            if (winCounter >= groupedByPersonHistoryBet.size() / SYSTEM_GROUP_HALF_DENOMINATOR) {
                final Person person = personService.findByLogin(winnerLogin);
                final int newBalance = person.getBalance() + winBetTotal * parlayCoefficient;
                final Person updatedPerson = new Person(person.getId(), person.getLogin(), person.getPassword(),
                        newBalance, person.getRole());

                winBetPersons.add(updatedPerson);
            }
        }

        for (Person winBetPerson : winBetPersons) {
            personService.updateBalance(winBetPerson);
        }
    }

    private void deleteBetRelatedInfo() throws ServiceException, DaoException {
        final List<Bet> bets = betService.findAll();
        final List<Betslip> betslips = betslipService.findAll();
        final List<Competition> competitions = competitionService.findAll();

        for (Bet bet : bets) {
            betService.delete(bet.getId());
        }

        for (Betslip betslip : betslips) {
            betslipService.delete(betslip.getId());
        }

        for (Competition competition : competitions) {
            competitionService.delete(competition.getId());
        }
    }

}