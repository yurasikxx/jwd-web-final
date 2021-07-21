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
import static com.epam.jwd.command.ShowRegisterPageCommand.REGISTER_JSP_PATH;

public class RegisterCommand implements Command {

    private static final String INVALID_LOGIN_MSG = "User with this login already exists";

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
        final String login = request.getParameter(LOGIN_PARAMETER_NAME);
        final String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        final Person person = new Person(login, password);

        try {
            if (!personService.canRegister(person)) {
                return prepareErrorPage(request);
            }

            personService.getNewRegisteredPersons(personService.save(person));
        } catch (DaoException | ServiceException e) {
            e.printStackTrace();
        }

        return registerSuccessCommandResponse;
    }

    private BaseCommandResponse prepareErrorPage(BaseCommandRequest request) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, INVALID_LOGIN_MSG);
        return registerErrorCommandResponse;
    }

}