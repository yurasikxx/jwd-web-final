package com.epam.jwd.command.add;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.INITIAL_BALANCE_VALUE;
import static com.epam.jwd.constant.Constant.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.LOGIN_REGEX;
import static com.epam.jwd.constant.Constant.MAX_LOGIN_LENGTH;
import static com.epam.jwd.constant.Constant.MAX_PASSWORD_LENGTH;
import static com.epam.jwd.constant.Constant.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PASSWORD_REGEX;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code PersonAddingCommand} class implements {@code Command}
 * interface and execute command that adds person.
 *
 * @see Command
 */
public class PersonAddingCommand implements Command {

    private static final String ADDING_INVALID_CREDENTIALS_MESSAGE_KEY = "credentials.adding.invalid";

    private static volatile PersonAddingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse successAddingCommandResponse;
    private final BaseCommandResponse errorAddingCommandResponse;

    private PersonAddingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.successAddingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorAddingCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
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
        return getCommandResponse(request);
    }

    private BaseCommandResponse getCommandResponse(BaseCommandRequest request) {
        try {
            final String login = getCheckedLogin(request);
            final String password = getCheckedPassword(request);
            final Person person = new Person(login, password, INITIAL_BALANCE_VALUE);

            if (!personService.canRegister(person)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ADDING_INVALID_CREDENTIALS_MESSAGE_KEY));
                request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return errorAddingCommandResponse;
            }

            personService.getNewRegisteredPersons(personService.register(person));
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorAddingCommandResponse;
        }

        return successAddingCommandResponse;
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

}