package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import static com.epam.jwd.command.CompetitionAddingCommand.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.command.CompetitionAddingCommand.SOMETHING_WENT_WRONG_MSG;
import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.LogInCommand.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.command.LogInCommand.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ADDING_JSP_PATH;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class PersonAddingCommand implements Command {

    private static final String PERSON_SUCCESSFULLY_ADDED_MSG = "Person successfully added";
    private static final String INVALID_CREDENTIALS_MSG = "Person with such login already exists or login or password are empty";
    private static volatile PersonAddingCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse;
    private final BaseCommandResponse personErrorCommandResponse;

    private PersonAddingCommand() {
        this.personService = PersonService.getInstance();
        this.personCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
        this.personErrorCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
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
                request.setAttribute(ERROR_ATTRIBUTE_NAME, SOMETHING_WENT_WRONG_MSG);
                request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return personErrorCommandResponse;
            }

            final String login = getCheckedLogin(request);
            final String password = getCheckedPassword(request);

            final Person person = new Person(login, password);

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
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

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