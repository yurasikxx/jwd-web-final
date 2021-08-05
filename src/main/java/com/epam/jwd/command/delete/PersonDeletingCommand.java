package com.epam.jwd.command.delete;

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

import java.util.List;

import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.FIELDS_FILLED_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_EMPTY_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;
import static com.epam.jwd.model.Role.USER;

public class PersonDeletingCommand implements Command {

    private static final String PERSON_SELECT_MESSAGE_KEY = "person.not.selected";
    private static final String PERSON_CANNOT_DELETE_MESSAGE_KEY = "person.cannot.delete";
    private static final String PERSON_DELETED_MESSAGE_KEY = "person.deleted";

    private static volatile PersonDeletingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private PersonDeletingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
    }

    public static PersonDeletingCommand getInstance() {
        if (instance == null) {
            synchronized (PersonDeletingCommand.class) {
                if (instance == null) {
                    instance = new PersonDeletingCommand();
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

            if (!personService.canBeDeleted(id)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(PERSON_SELECT_MESSAGE_KEY));
                request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));

                return personCommandResponse;
            }

            personService.delete(id);

            final List<Person> users = personService.findByRole(USER);

            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(PERSON_DELETED_MESSAGE_KEY));
            request.setAttribute(SELECT_PERSON_ATTRIBUTE_NAME, users);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(FIELDS_FILLED_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ID_EMPTY_MESSAGE_KEY));
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(PERSON_CANNOT_DELETE_MESSAGE_KEY));
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

}