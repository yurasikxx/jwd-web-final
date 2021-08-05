package com.epam.jwd.command.change;

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
import com.epam.jwd.model.Role;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.NUMBERS_POSITIVE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

public class PersonChangingCommand implements Command {

    private static final String PERSON_CHANGED_MESSAGE_KEY = "person.changed";
    private static final String BALANCE_PARAMETER_NAME = "balance";

    private static volatile PersonChangingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse;

    private PersonChangingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.personCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
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
        return getCommandResponse(request);
    }

    private BaseCommandResponse getCommandResponse(BaseCommandRequest request) {
        try {
            final Long id = getCheckedId(request);
            final String login = getCheckedLogin(request);
            final String password = getCheckedPassword(request);
            final Integer balance = getCheckedBalance(request);

            if (id < INITIAL_INDEX_VALUE || balance < INITIAL_INDEX_VALUE) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(NUMBERS_POSITIVE_MESSAGE_KEY));
                request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return personCommandResponse;
            }

            final Person person = new Person(id, login, password, balance, Role.USER);

            personService.update(person);

            final List<Person> persons = personService.findAll();

            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(PERSON_CHANGED_MESSAGE_KEY));
            request.setAttribute(SELECT_PERSON_ATTRIBUTE_NAME, persons);
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        }

        return personCommandResponse;
    }

    private Long getCheckedId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private String getCheckedLogin(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final String login;

        if (request.getParameter(LOGIN_PARAMETER_NAME) != null) {
            login = request.getParameter(LOGIN_PARAMETER_NAME);
            return login;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private String getCheckedPassword(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final String password;

        if (request.getParameter(PASSWORD_PARAMETER_NAME) != null) {
            password = request.getParameter(PASSWORD_PARAMETER_NAME);
            return password;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private Integer getCheckedBalance(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final int balance;

        if (request.getParameter(BALANCE_PARAMETER_NAME) != null) {
            balance = Integer.parseInt(request.getParameter(BALANCE_PARAMETER_NAME));
            return balance;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

}