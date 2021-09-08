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
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.MAX_PASSWORD_LENGTH;
import static com.epam.jwd.constant.Constant.PASSWORD_CHANGING_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PASSWORD_REGEX;
import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code PasswordChangingOperation} class implements {@code Command}
 * interface and execute command that changes person.
 *
 * @see Command
 */
public class PasswordChangingCommand implements Command {

    private static volatile PasswordChangingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse successChangingCommandResponse;
    private final BaseCommandResponse errorChangingCommandResponse;

    private PasswordChangingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.successChangingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorChangingCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static PasswordChangingCommand getInstance() {
        if (instance == null) {
            synchronized (PasswordChangingCommand.class) {
                if (instance == null) {
                    instance = new PasswordChangingCommand();
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
            final String password = getCheckedPassword(request);
            final HttpSession session;
            String login = null;

            if (request.getCurrentSession().isPresent()) {
                session = request.getCurrentSession().get();
                login = (String) session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME);
            }

            final Person currentPerson = personService.findByLogin(login);

            personService.changePassword(currentPerson, password);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(PASSWORD_CHANGING_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorChangingCommandResponse;
        } catch (ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(PASSWORD_CHANGING_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return errorChangingCommandResponse;
        }

        return successChangingCommandResponse;
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