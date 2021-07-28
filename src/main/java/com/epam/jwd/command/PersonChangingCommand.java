package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.LogInCommand.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.command.LogInCommand.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowPersonAddingPageCommand.ENTER_PERSON_LOGIN_MSG;
import static com.epam.jwd.command.ShowPersonAddingPageCommand.ENTER_PERSON_PASSWORD_MSG;
import static com.epam.jwd.command.ShowPersonAddingPageCommand.LOGIN_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonAddingPageCommand.PASSWORD_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.BALANCE_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.CHANGING_JSP_PATH;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.ENTER_ID_OF_CHANGEABLE_PERSON_MSG;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.ENTER_PERSON_BALANCE_MSG;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.ID_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class PersonChangingCommand implements Command {

    private static final String PERSON_SUCCESSFULLY_CHANGED_MSG = "Person successfully changed";
    private static final String BALANCE_PARAMETER_NAME = "balance";

    private static volatile PersonChangingCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse;
    private final BaseCommandResponse personErrorCommandResponse;


    private PersonChangingCommand() {
        this.personService = PersonService.getInstance();
        this.personCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
        this.personErrorCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static PersonChangingCommand getInstance() {
        if (instance == null) {
            synchronized (PersonChangingCommand.class) {
                if (instance == null) {
                    instance = new PersonChangingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            if (getCheckedId(request) == null
                    || getCheckedLogin(request) == null
                    || getCheckedPassword(request) == null
                    || getCheckedBalance(request) == null) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
                request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return personErrorCommandResponse;
            }

            final Long id = getCheckedId(request);
            final String login = getCheckedLogin(request);
            final String password = getCheckedPassword(request);
            final Double balance = getCheckedBalance(request);

            final Person person = new Person(id, login, password, balance, Role.USER);

            personService.update(person);
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return personErrorCommandResponse;
        }

        request.setAttribute(PERSON_ATTRIBUTE_NAME, PERSON_SUCCESSFULLY_CHANGED_MSG);
        request.setAttribute(ID_ATTRIBUTE_NAME, ENTER_ID_OF_CHANGEABLE_PERSON_MSG);
        request.setAttribute(LOGIN_ATTRIBUTE_NAME, ENTER_PERSON_LOGIN_MSG);
        request.setAttribute(PASSWORD_ATTRIBUTE_NAME, ENTER_PERSON_PASSWORD_MSG);
        request.setAttribute(BALANCE_ATTRIBUTE_NAME, ENTER_PERSON_BALANCE_MSG);

        return personCommandResponse;
    }

    private Long getCheckedId(BaseCommandRequest request) {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        return null;
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

    private Double getCheckedBalance(BaseCommandRequest request) {
        final double balance;

        if (request.getParameter(BALANCE_PARAMETER_NAME) != null) {
            balance = Integer.parseInt(request.getParameter(BALANCE_PARAMETER_NAME));
            return balance;
        }

        return null;
    }

}