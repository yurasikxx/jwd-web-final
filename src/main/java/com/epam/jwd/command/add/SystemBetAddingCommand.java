package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.BETSLIP_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.BET_TOTAL_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.EMPTY_LIST_SIZE_VALUE;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.INCORRECT_DATA_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.NUMBERS_POSITIVE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.PARLAY_BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.PERSON_BALANCE_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.PERSON_HAS_BET_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.SYSTEM_BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;
import static com.epam.jwd.model.BetType.SYSTEM;

/**
 * A {@code SystemBetAddingCommand} class implements {@code Command}
 * interface and execute command that adds system bet.
 *
 * @see Command
 */
public class SystemBetAddingCommand implements Command {

    private static final String PERSON_BALANCE_MESSAGE_KEY = "person.balance";
    private static final String SAME_COMPETITION_BETSLIP_MESSAGE_KEY = "betslip.system.same.competition";
    private static final int MIN_SYSTEM_BETSLIPS_VALUE = 3;
    private static final String WRONG_SYSTEM_BETSLIPS_MESSAGE_KEY = "betslip.system.wrong.value";
    private static final String BETSLIP_SYSTEM_AMOUNT_MESSAGE_KEY = "betslip.system.amount";

    private static volatile SystemBetAddingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BetslipBaseService betslipService;
    private final BetBaseService betService;
    private final BaseCommandResponse successAddingCommandResponse;
    private final BaseCommandResponse errorAddingCommandResponse;

    private SystemBetAddingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betService = BetService.getInstance();
        this.successAddingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorAddingCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static SystemBetAddingCommand getInstance() {
        if (instance == null) {
            synchronized (SystemBetAddingCommand.class) {
                if (instance == null) {
                    instance = new SystemBetAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            final HttpSession currentSession = getCurrentSession(request);
            final String currentPersonLogin = extractPersonNameFromSession(currentSession);
            final Integer betTotal = getCheckedBetTotal(request);
            final List<Long> betslipIds = getCheckedBetslipIds(request);
            final List<Bet> systemBets = betService.findByBetType(SYSTEM);
            final List<Betslip> betslips = new ArrayList<>();
            final List<Bet> savedBets = new ArrayList<>();
            Person updatedPerson = null;

            if (checkSystemPresence(request, currentPersonLogin, systemBets)) {
                return errorAddingCommandResponse;
            }

            fillBetslips(betslipIds, betslips);

            for (Betslip betslip : betslips) {
                final Person person = personService.findByLogin(currentPersonLogin);
                final Bet bet = new Bet(betslip, betTotal, SYSTEM, person);
                final Person placedBetPerson = new Person(person.getId(), person.getLogin(), person.getPassword(),
                        person.getBalance() - Objects.requireNonNull(betTotal), person.getRole());

                if (cannotBeSaved(request, betslipIds, betslip, betslips, betTotal, person, bet)) {
                    return errorAddingCommandResponse;
                }

                savedBets.add(bet);
                updatedPerson = placedBetPerson;
            }

            saveCheckedBets(savedBets);
            updateSessionInfo(currentSession, updatedPerson);
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(INCORRECT_DATA_MESSAGE_KEY));
            request.setAttribute(SYSTEM_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(SYSTEM_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        }

        return successAddingCommandResponse;
    }

    private HttpSession getCurrentSession(BaseCommandRequest request) {
        final Optional<HttpSession> session = request.getCurrentSession();
        HttpSession currentSession = null;

        if (session.isPresent()) {
            currentSession = session.get();
        }
        return currentSession;
    }

    private String extractPersonNameFromSession(HttpSession session) {
        return session != null && session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME) != null
                ? (String) session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME)
                : Role.UNAUTHORIZED.getName();
    }

    private List<Long> getCheckedBetslipIds(BaseCommandRequest request) throws IncorrectEnteredDataException {
        if (request.getParameter(BETSLIP_PARAMETER_NAME) != null) {
            return Arrays.stream(request.getParameterValues(BETSLIP_PARAMETER_NAME))
                    .map(Long::valueOf)
                    .collect((Collectors.toList()));
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private Integer getCheckedBetTotal(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final int betTotal;

        if (request.getParameter(BET_TOTAL_PARAMETER_NAME) != null) {
            betTotal = Integer.parseInt(request.getParameter(BET_TOTAL_PARAMETER_NAME));
            return betTotal;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private boolean checkSystemPresence(BaseCommandRequest request, String currentPersonLogin, List<Bet> systemBets) {
        for (Bet systemBet : systemBets) {
            if (systemBet.getPerson().getLogin().equals(currentPersonLogin)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_SYSTEM_AMOUNT_MESSAGE_KEY));
                request.setAttribute(PARLAY_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return true;
            }
        }
        return false;
    }

    private void fillBetslips(List<Long> betslipIds, List<Betslip> betslips) {
        for (Long betslipId : betslipIds) {
            betslips.add(betslipService.findById(betslipId));
        }
    }

    private boolean cannotBeSaved(BaseCommandRequest request, List<Long> betslipIds, Betslip betslip,
                                  List<Betslip> betslips, Integer betTotal, Person person, Bet bet) {
        if (!(betslipIds.size() >= MIN_SYSTEM_BETSLIPS_VALUE)) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(WRONG_SYSTEM_BETSLIPS_MESSAGE_KEY));
            request.setAttribute(SYSTEM_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (checkIfSame(request, betslip, betslips)) {
            return true;
        }

        if (betTotal < MIN_LONG_ID_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(NUMBERS_POSITIVE_MESSAGE_KEY));
            request.setAttribute(SYSTEM_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (person.getBalance() - betTotal < MIN_INDEX_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(PERSON_BALANCE_MESSAGE_KEY));
            request.setAttribute(SYSTEM_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (!betService.canSave(bet)) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(PERSON_HAS_BET_MESSAGE_KEY));
            request.setAttribute(SYSTEM_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        return false;
    }

    private boolean checkIfSame(BaseCommandRequest request, Betslip betslip, List<Betslip> betslips) {
        int counter = EMPTY_LIST_SIZE_VALUE;

        for (Betslip iteratedBetslip : betslips) {
            if (iteratedBetslip.getCompetition().equals(betslip.getCompetition())) {
                counter++;
            }

            if (counter > INITIAL_INDEX_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(SAME_COMPETITION_BETSLIP_MESSAGE_KEY));
                request.setAttribute(PARLAY_BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return true;
            }
        }
        return false;
    }

    private void saveCheckedBets(List<Bet> savedBets) throws ServiceException, DaoException {
        for (Bet savedBet : savedBets) {
            betService.save(savedBet);
        }
    }

    private void updateSessionInfo(HttpSession currentSession, Person updatedPerson) throws DaoException, ServiceException {
        personService.updateBalance(updatedPerson);
        Objects.requireNonNull(currentSession).setAttribute(PERSON_BALANCE_SESSION_ATTRIBUTE_NAME,
                Objects.requireNonNull(updatedPerson).getBalance());
    }

}