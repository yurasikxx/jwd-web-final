package com.epam.jwd.command.auth;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import javax.servlet.http.HttpSession;

import static com.epam.jwd.constant.Constant.EMPTY_CREDENTIALS_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.INDEX_JSP_PATH;
import static com.epam.jwd.constant.Constant.LOCALE_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.LOGIN_JSP_PATH;
import static com.epam.jwd.constant.Constant.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.PERSON_BALANCE_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ROLE_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

public class LogInCommand implements Command {

    private static final String INVALID_CREDENTIALS_MESSAGE_KEY = "credentials.auth.invalid";

    private static volatile LogInCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse loginSuccessCommandResponse;
    private final BaseCommandResponse loginErrorCommandResponse;

    private LogInCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.loginSuccessCommandResponse = new CommandResponse(INDEX_JSP_PATH, true);
        this.loginErrorCommandResponse = new CommandResponse(LOGIN_JSP_PATH, false);
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
        return getCommandResponse(request);
    }

    private BaseCommandResponse getCommandResponse(BaseCommandRequest request) {
        try {
            final String login = getCheckedLogin(request);
            final String password = getCheckedPassword(request);
            final Integer balance = personService.findByLogin(login).getBalance();

            final Person person = new Person(login, password, balance);

            if (!personService.canLogIn(person)) {
                return prepareErrorPage(request);
            }

            return addPersonInfoToSession(request, login);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(EMPTY_CREDENTIALS_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return loginErrorCommandResponse;
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return loginErrorCommandResponse;
        }
    }

    private String getCheckedLogin(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final String login;

        if (request.getParameter(LOGIN_PARAMETER_NAME) != null) {
            login = request.getParameter(LOGIN_PARAMETER_NAME);
            return login;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(EMPTY_CREDENTIALS_MESSAGE_KEY));
    }

    private String getCheckedPassword(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final String password;

        if (request.getParameter(PASSWORD_PARAMETER_NAME) != null) {
            password = request.getParameter(PASSWORD_PARAMETER_NAME);
            return password;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(EMPTY_CREDENTIALS_MESSAGE_KEY));
    }

    private BaseCommandResponse prepareErrorPage(BaseCommandRequest request) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(INVALID_CREDENTIALS_MESSAGE_KEY));
        request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

        return loginErrorCommandResponse;
    }

    private BaseCommandResponse addPersonInfoToSession(BaseCommandRequest request, String login) {
        try {
            final HttpSession currentSession;
            String locale = null;

            if (request.getCurrentSession().isPresent()) {
                currentSession = request.getCurrentSession().get();
                locale = currentSession.getAttribute(LOCALE_PARAMETER_NAME).toString();
            }

            request.getCurrentSession().ifPresent(HttpSession::invalidate);

            final HttpSession session = request.createSession();
            final Person loggedInPerson = personService.findByLogin(login);

            session.setAttribute(LOCALE_PARAMETER_NAME, locale);
            session.setAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME, loggedInPerson.getLogin());
            session.setAttribute(PERSON_ROLE_SESSION_ATTRIBUTE_NAME, loggedInPerson.getRole());
            session.setAttribute(PERSON_BALANCE_SESSION_ATTRIBUTE_NAME, loggedInPerson.getBalance());
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return loginErrorCommandResponse;
        }

        return loginSuccessCommandResponse;
    }

}