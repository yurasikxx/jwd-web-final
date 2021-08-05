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

import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.INDEX_JSP_PATH;
import static com.epam.jwd.constant.Constant.INITIAL_BALANCE_VALUE;
import static com.epam.jwd.constant.Constant.LOGIN_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PASSWORD_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.REGISTER_JSP_PATH;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

public class RegisterCommand implements Command {

    private static final String REGISTRATION_INVALID_CREDENTIALS_MESSAGE_KEY = "credentials.registration.invalid";

    private static volatile RegisterCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse registerSuccessCommandResponse = new CommandResponse(INDEX_JSP_PATH, true);
    private final BaseCommandResponse registerErrorCommandResponse = new CommandResponse(REGISTER_JSP_PATH, false);

    private RegisterCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
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
        return getCommandResponse(request);
    }

    private BaseCommandResponse getCommandResponse(BaseCommandRequest request) {
        try {
            final String login = getCheckedLogin(request);
            final String password = getCheckedPassword(request);
            final Person person = new Person(login, password, INITIAL_BALANCE_VALUE);

            if (!personService.canRegister(person)) {
                return prepareErrorPage(request);
            }

            personService.getNewRegisteredPersons(personService.register(person));

            return registerSuccessCommandResponse;
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(REGISTRATION_INVALID_CREDENTIALS_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return registerErrorCommandResponse;
        } catch (DaoException | ServiceException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

            return registerErrorCommandResponse;
        }
    }

    private String getCheckedLogin(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final String login;

        if (request.getParameter(LOGIN_PARAMETER_NAME) != null) {
            login = request.getParameter(LOGIN_PARAMETER_NAME);
            return login;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(REGISTRATION_INVALID_CREDENTIALS_MESSAGE_KEY));
    }

    private String getCheckedPassword(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final String password;

        if (request.getParameter(PASSWORD_PARAMETER_NAME) != null) {
            password = request.getParameter(PASSWORD_PARAMETER_NAME);
            return password;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(REGISTRATION_INVALID_CREDENTIALS_MESSAGE_KEY));
    }

    private BaseCommandResponse prepareErrorPage(BaseCommandRequest request) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(REGISTRATION_INVALID_CREDENTIALS_MESSAGE_KEY));
        return registerErrorCommandResponse;
    }

}