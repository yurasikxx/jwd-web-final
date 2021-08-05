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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.BETSLIP_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.BET_TOTAL_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.INCORRECT_DATA_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_LONG_ID_VALUE;
import static com.epam.jwd.constant.Constant.NUMBERS_POSITIVE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.PERSON_BALANCE_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.PERSON_HAS_BET_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

public class BetAddingCommand implements Command {

    private static final String BET_ADDED_MESSAGE_KEY = "bet.added";
    private static final String PERSON_BALANCE_MESSAGE_KEY = "person.balance";
    private static final String BETSLIP_SELECT_MESSAGE_KEY = "betslip.not.selected";

    private static volatile BetAddingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BetslipBaseService betslipService;
    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse;

    private BetAddingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betService = BetService.getInstance();
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static BetAddingCommand getInstance() {
        if (instance == null) {
            synchronized (BetAddingCommand.class) {
                if (instance == null) {
                    instance = new BetAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            final Optional<HttpSession> session = request.getCurrentSession();
            HttpSession currentSession = null;

            if (session.isPresent()) {
                currentSession = session.get();
            }

            final String currentPersonLogin = extractPersonNameFromSession(currentSession);
            final Long betslipId = getCheckedBetslipId(request);
            final Integer betTotal = getCheckedBetTotal(request);
            final Betslip betslip = betslipService.findById(betslipId);
            final Person person = personService.findByLogin(currentPersonLogin);
            final Bet bet = new Bet(betslip, betTotal, person);
            final Person placedBetPerson = new Person(person.getId(), person.getLogin(), person.getPassword(),
                    person.getBalance() - Objects.requireNonNull(betTotal), person.getRole());

            if (cannotBeSaved(request, betslipId, betTotal, person, bet)) {
                return betCommandResponse;
            }

            betService.save(bet);
            personService.updateBalance(placedBetPerson);

            final List<Betslip> betslips = betslipService.findAll()
                    .stream()
                    .sorted(Comparator.comparing(o -> o.getCompetition().toString()))
                    .collect(Collectors.toList());

            Objects.requireNonNull(currentSession).setAttribute(PERSON_BALANCE_SESSION_ATTRIBUTE_NAME,
                    placedBetPerson.getBalance());
            request.setAttribute(BET_ATTRIBUTE_NAME, messageManager.getString(BET_ADDED_MESSAGE_KEY));
            request.setAttribute(SELECT_BETSLIP_ATTRIBUTE_NAME, betslips);
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(INCORRECT_DATA_MESSAGE_KEY));
            request.setAttribute(BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        }

        return betCommandResponse;
    }

    private boolean cannotBeSaved(BaseCommandRequest request, Long betslipId, Integer betTotal, Person person, Bet bet) {
        if (betslipId < MIN_LONG_ID_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(BETSLIP_SELECT_MESSAGE_KEY));
            request.setAttribute(BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (betTotal < MIN_LONG_ID_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(NUMBERS_POSITIVE_MESSAGE_KEY));
            request.setAttribute(BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (person.getBalance() - betTotal < MIN_INDEX_VALUE) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(PERSON_BALANCE_MESSAGE_KEY));
            request.setAttribute(BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }

        if (!betService.canSave(bet)) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(PERSON_HAS_BET_MESSAGE_KEY));
            request.setAttribute(BET_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return true;
        }
        return false;
    }

    private Long getCheckedBetslipId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(BETSLIP_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(BETSLIP_PARAMETER_NAME));
            return id;
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

    private String extractPersonNameFromSession(HttpSession session) {
        return session != null && session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME) != null
                ? (String) session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME)
                : Role.UNAUTHORIZED.getName();
    }

}