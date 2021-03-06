package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.LOGIN_REGEX;
import static com.epam.jwd.constant.Constant.MAX_LOGIN_LENGTH;
import static com.epam.jwd.constant.Constant.MAX_PASSWORD_LENGTH;
import static com.epam.jwd.constant.Constant.NUMBERS_POSITIVE_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PASSWORD_REGEX;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.REGISTRATION_INVALID_CREDENTIALS_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code PersonChangingCommand} class implements {@code Command}
 * interface and execute command that changes person.
 *
 * @see Command
 */
public class PersonChangingCommand implements Command {

    private static final String BALANCE_PARAMETER_NAME = "balance";

    private static volatile PersonChangingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse successChangingCommandResponse;
    private final BaseCommandResponse errorChangingCommandResponse;

    private PersonChangingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.successChangingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorChangingCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
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

                return errorChangingCommandResponse;
            }

            final Person person = new Person(id, login, password, balance, Role.USER);

            personService.update(person);
        } catch (IncorrectEnteredDataException | NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorChangingCommandResponse;
        } catch (ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(REGISTRATION_INVALID_CREDENTIALS_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorChangingCommandResponse;
        }

        return successChangingCommandResponse;
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
        final Pattern pattern = Pattern.compile(LOGIN_REGEX);

        if (request.getParameter(LOGIN_PARAMETER_NAME) != null) {
            final String login = request.getParameter(LOGIN_PARAMETER_NAME);
            final Matcher matcher = pattern.matcher(login);

            if (matcher.matches() && login.length() <= MAX_LOGIN_LENGTH) {
                return login;
            }
        }

        throw new IncorrectEnteredDataException(messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
    }

    private String getCheckedPassword(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

        if (request.getParameter(PASSWORD_PARAMETER_NAME) != null) {
            final String password = request.getParameter(PASSWORD_PARAMETER_NAME);
            final Matcher matcher = pattern.matcher(password);

            if (matcher.matches() && password.length() <= MAX_PASSWORD_LENGTH) {
                return password;
            }
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