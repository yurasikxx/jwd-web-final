package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import javax.servlet.http.HttpSession;

import static com.epam.jwd.command.ShowLogInPageCommand.LOGIN_JSP_PATH;

public class LogInCommand implements Command {

    public static final String PERSON_ROLE_SESSION_ATTRIBUTE_NAME = "personRole";

    protected static final String PERSON_NAME_SESSION_ATTRIBUTE_NAME = "personName";
    protected static final String INDEX_JSP_PATH = "/index.jsp";
    protected static final String LOGIN_PARAMETER_NAME = "login";
    protected static final String PASSWORD_PARAMETER_NAME = "password";
    protected static final String ERROR_ATTRIBUTE_NAME = "error";

    private static final String INVALID_CREDENTIALS_MSG = "Wrong login or password";

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
        final String login = request.getParameter(LOGIN_PARAMETER_NAME);
        final String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        final Person person = new Person(login, password);

        if (!personService.canLogIn(person)) {
            return prepareErrorPage(request);
        }

        return addPersonInfoToSession(request, login);
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