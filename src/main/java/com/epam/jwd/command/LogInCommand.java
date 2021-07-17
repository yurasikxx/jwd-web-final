package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import javax.servlet.http.HttpSession;

public class LogInCommand implements Command {

    private static final String LOGIN_PARAMETER_NAME = "login";
    private static final String PASSWORD_PARAMETER_NAME = "password";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String INVALID_CREDENTIALS_MSG = "wrong login or password";
    private static final String ERROR_LOGIN_JSP_PATH = "/WEB-INF/jsp/login.jsp";
    private static final String USER_SESSION_ATTRIBUTE_NAME = "person";
    private static final String SUCCESS_LOGIN_JSP_PATH = "/index.jsp";

    private static volatile LogInCommand instance;
    private final PersonBaseService personService;
    private final BaseCommandResponse loginSuccessResponse = new CommandResponse(SUCCESS_LOGIN_JSP_PATH, true);
    private final BaseCommandResponse loginErrorCommandResponse = new CommandResponse(ERROR_LOGIN_JSP_PATH, false);

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
        for (int i = 0; i < personService.findAll().size(); i++) {
            try {
                personService.update(personService.findAll().get(i));
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }

        final String login = request.getParameter(LOGIN_PARAMETER_NAME);
        final String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        final Person person = new Person(login, password);

        if (!personService.canLogIn(person)) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, INVALID_CREDENTIALS_MSG);
            return loginErrorCommandResponse;
        }

        request.getCurrentSession().ifPresent(HttpSession::invalidate);
        final HttpSession session = request.createSession();
        session.setAttribute(USER_SESSION_ATTRIBUTE_NAME, login);
        return loginSuccessResponse;
    }

}
