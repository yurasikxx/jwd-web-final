package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.LogInCommand.INDEX_JSP_PATH;
import static com.epam.jwd.command.LogInCommand.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.command.LogInCommand.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.command.PersonAddingCommand.INITIAL_BALANCE_VALUE;
import static com.epam.jwd.command.ShowRegisterPageCommand.REGISTER_JSP_PATH;

public class RegisterCommand implements Command {

    private static final String INVALID_CREDENTIALS_MSG = "Credentials must not be empty or user with such login already exists";

    private static volatile RegisterCommand instance;
    private final PersonBaseService personService;
    private final BaseCommandResponse registerSuccessCommandResponse = new CommandResponse(INDEX_JSP_PATH, true);
    private final BaseCommandResponse registerErrorCommandResponse = new CommandResponse(REGISTER_JSP_PATH, false);

    private RegisterCommand() {
        this.personService = PersonService.getInstance();
    }

    public static RegisterCommand getInstance() {
        if (instance == null) {
            synchronized (RegisterCommand.class) {
                if (instance == null) {
                    instance = new RegisterCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        if (getCheckedLogin(request) == null || getCheckedPassword(request) == null) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, INVALID_CREDENTIALS_MSG);
            return registerErrorCommandResponse;
        }

        final String login = getCheckedLogin(request);
        final String password = getCheckedPassword(request);
        final Person person = new Person(login, password, INITIAL_BALANCE_VALUE);

        try {
            if (!personService.canRegister(person)) {
                return prepareErrorPage(request);
            }

            personService.getNewRegisteredPersons(personService.register(person));
        } catch (DaoException | ServiceException e) {
            e.printStackTrace();
        }

        return registerSuccessCommandResponse;
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
        return registerErrorCommandResponse;
    }

}