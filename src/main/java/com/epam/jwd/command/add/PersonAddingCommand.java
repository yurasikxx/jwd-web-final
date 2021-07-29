package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.INITIAL_BALANCE_VALUE;
import static com.epam.jwd.constant.Constant.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class PersonAddingCommand implements Command {

    private static final String PERSON_SUCCESSFULLY_ADDED_MSG = "Person successfully added";
    private static final String INVALID_CREDENTIALS_MSG = "Person with such login already exists or login/password are empty";

    private static volatile PersonAddingCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse;
    private final BaseCommandResponse personErrorCommandResponse;

    private PersonAddingCommand() {
        this.personService = PersonService.getInstance();
        this.personCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.personErrorCommandResponse = personCommandResponse;
    }

    public static PersonAddingCommand getInstance() {
        if (instance == null) {
            synchronized (PersonAddingCommand.class) {
                if (instance == null) {
                    instance = new PersonAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedLogin(request) == null ||
                    getCheckedPassword(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, INVALID_CREDENTIALS_MSG);
                request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return personErrorCommandResponse;
            }

            final String login = getCheckedLogin(request);
            final String password = getCheckedPassword(request);

            final Person person = new Person(login, password, INITIAL_BALANCE_VALUE);

            if (!personService.canRegister(person)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, INVALID_CREDENTIALS_MSG);
                request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return personErrorCommandResponse;
            }

            personService.getNewRegisteredPersons(personService.register(person));
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return personErrorCommandResponse;
        }

        request.setAttribute(PERSON_ATTRIBUTE_NAME, PERSON_SUCCESSFULLY_ADDED_MSG);

        return personCommandResponse;
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

}