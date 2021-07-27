package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import javax.servlet.http.HttpSession;

import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowLogInPageCommand.LOGIN_JSP_PATH;
import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class LogInCommand implements Command {

    public static final String PERSON_ROLE_SESSION_ATTRIBUTE_NAME = "personRole";

    protected static final String PERSON_NAME_SESSION_ATTRIBUTE_NAME = "personName";
    protected static final String INDEX_JSP_PATH = "/index.jsp";
    protected static final String LOGIN_PARAMETER_NAME = "login";
    protected static final String PASSWORD_PARAMETER_NAME = "password";
    protected static final String ERROR_ATTRIBUTE_NAME = "error";
    protected static final String EMPTY_CREDENTIALS_MSG = "Credentials must not be empty";

    private static final String INVALID_CREDENTIALS_MSG = "Credentials must not be empty or wrong login or password";

    private static volatile LogInCommand instance;
    private final PersonBaseService personService;
    private final BaseCommandResponse loginSuccessCommandResponse = new CommandResponse(INDEX_JSP_PATH, true);
    private final BaseCommandResponse loginErrorCommandResponse = new CommandResponse(LOGIN_JSP_PATH, false);

    private LogInCommand() {
        this.personService = PersonService.getInstance();
    }

    public static LogInCommand getInstance() {
        if (instance == null) {
            synchronized (LogInCommand.class) {
                if (instance == null) {
                    instance = new LogInCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        if (getCheckedLogin(request) == null || getCheckedPassword(request) == null) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, EMPTY_CREDENTIALS_MSG);
            return loginErrorCommandResponse;
        }

        final String login = getCheckedLogin(request);
        final String password = getCheckedPassword(request);
        final Integer balance;

        try {
            balance = personService.findByLogin(login).getBalance();
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return loginErrorCommandResponse;
        }

        final Person person = new Person(login, password, balance);

        if (!personService.canLogIn(person)) {
            return prepareErrorPage(request);
        }

        return addPersonInfoToSession(request, login);
    }

    private String getCheckedLogin(BaseCommandRequest request) {
        final String login;

        if (request.getParameter(LOGIN_PARAMETER_NAME) != null) {
            login = request.getParameter(LOGIN_PARAMETER_NAME);
            return login;
        }

        return null;
    }

    private String getCheckedPassword(BaseCommandRequest request) {
        final String password;

        if (request.getParameter(PASSWORD_PARAMETER_NAME) != null) {
            password = request.getParameter(PASSWORD_PARAMETER_NAME);
            return password;
        }

        return null;
    }

    private BaseCommandResponse prepareErrorPage(BaseCommandRequest request) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, INVALID_CREDENTIALS_MSG);
        return loginErrorCommandResponse;
    }

    private BaseCommandResponse addPersonInfoToSession(BaseCommandRequest request, String login) {
        try {
            request.getCurrentSession().ifPresent(HttpSession::invalidate);

            final HttpSession session = request.createSession();
            final Person loggedInPerson = personService.findByLogin(login);

            session.setAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME, loggedInPerson.getLogin());
            session.setAttribute(PERSON_ROLE_SESSION_ATTRIBUTE_NAME, loggedInPerson.getRole());
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
        }

        return loginSuccessCommandResponse;
    }

}