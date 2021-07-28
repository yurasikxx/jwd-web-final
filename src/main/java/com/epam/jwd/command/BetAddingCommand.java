package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
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
import java.util.Objects;
import java.util.Optional;

import static com.epam.jwd.command.BetslipAddingCommand.INCORRECT_ENTERED_DATA;
import static com.epam.jwd.command.BetslipAddingCommand.NUMBERS_MUST_BE_POSITIVE_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.MIN_ELEMENT_ID;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.LogInCommand.PERSON_BALANCE_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.LogInCommand.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowAllBetsListPageCommand.BET_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetAddingPageCommand.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetAddingPageCommand.BET_TOTAL_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_BETSLIP_ID_MSG;
import static com.epam.jwd.command.ShowBetAddingPageCommand.ENTER_BET_TOTAL_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;

public class BetAddingCommand implements Command {

    protected static final String BETSLIP_PARAMETER_NAME = "betslipId";
    protected static final String BET_TOTAL_PARAMETER_NAME = "betTotal";
    protected static final String PERSON_PARAMETER_NAME = "personId";
    protected static final String BET_SUCCESSFULLY_ADDED_MSG = "Bet successfully added";
    protected static final String BETSLIP_DOES_NOT_EXIST_MSG = "Betslip with such ID doesn't exist";
    protected static final String PERSON_DOES_NOT_EXIST_MSG = "Person with such ID doesn't exist";
    protected static final String PERSON_ALREADY_HAS_BET_WITH_SUCH_BETSLIP_MSG = "Person already has bet with such betslip";
    private static final int ZERO_ELEMENT_VALUE = 0;
    private static final String INSUFFICIENT_FUNDS_MSG = "Insufficient funds on the balance";

    private static volatile BetAddingCommand instance;

    private final PersonBaseService personService;
    private final BetslipBaseService betslipService;
    private final BetBaseService betService;
    private final BaseCommandResponse betCommandResponse;
    private final BaseCommandResponse betErrorCommandResponse;

    private BetAddingCommand() {
        this.personService = PersonService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.betService = BetService.getInstance();
        this.betCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.betErrorCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
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
            if (getCheckedBetslipId(request) == null
                    || getCheckedBetTotal(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            if (Objects.requireNonNull(getCheckedBetslipId(request)) < MIN_ELEMENT_ID
                    || Objects.requireNonNull(getCheckedBetTotal(request)) < MIN_ELEMENT_ID) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, NUMBERS_MUST_BE_POSITIVE_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            if (!betslipService.findAll()
                    .contains(betslipService.findById(Objects.requireNonNull(getCheckedBetslipId(request))))) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, BETSLIP_DOES_NOT_EXIST_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

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

            if (person.getBalance() - betTotal < ZERO_ELEMENT_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, INSUFFICIENT_FUNDS_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            if (!betService.canSave(bet)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, PERSON_ALREADY_HAS_BET_WITH_SUCH_BETSLIP_MSG);
                request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return betErrorCommandResponse;
            }

            betService.save(bet);
            personService.updateBalance(placedBetPerson);

            Objects.requireNonNull(currentSession).setAttribute(PERSON_BALANCE_SESSION_ATTRIBUTE_NAME,
                    placedBetPerson.getBalance());
        } catch (DaoException | ServiceException e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, INCORRECT_ENTERED_DATA);
            request.setAttribute(BET_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return betErrorCommandResponse;
        }

        request.setAttribute(BET_ATTRIBUTE_NAME, BET_SUCCESSFULLY_ADDED_MSG);
        request.setAttribute(BETSLIP_ATTRIBUTE_NAME, ENTER_BETSLIP_ID_MSG);
        request.setAttribute(BET_TOTAL_ATTRIBUTE_NAME, ENTER_BET_TOTAL_MSG);

        return betCommandResponse;
    }

    private Long getCheckedBetslipId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(BETSLIP_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(BETSLIP_PARAMETER_NAME));
            return id;
        }

        return null;
    }

    private Integer getCheckedBetTotal(BaseCommandRequest request) {
        final int betTotal;

        if (request.getParameter(BET_TOTAL_PARAMETER_NAME) != null) {
            betTotal = Integer.parseInt(request.getParameter(BET_TOTAL_PARAMETER_NAME));
            return betTotal;
        }

        return null;
    }

    private String extractPersonNameFromSession(HttpSession session) {
        return session != null && session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME) != null
                ? (String) session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME)
                : Role.UNAUTHORIZED.getName();
    }

}