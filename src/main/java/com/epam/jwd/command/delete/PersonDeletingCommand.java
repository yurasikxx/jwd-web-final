package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

import static com.epam.jwd.constant.Constant.ALL_FIELDS_MUST_BE_FILLED_MSG;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;
import static com.epam.jwd.model.Role.USER;

public class PersonDeletingCommand implements Command {

    private static final String PERSON_NOT_SELECTED_MSG = "Person not selected";
    private static final String PERSON_CANNOT_BE_DELETED_MSG = "Person cannot be deleted while there is unplayed bet";
    private static final String PERSON_SUCCESSFULLY_DELETED_MSG = "Person successfully deleted";

    private static volatile PersonDeletingCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private PersonDeletingCommand() {
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
                request.setAttribute(ERROR_ATTRIBUTE_NAME, PERSON_NOT_SELECTED_MSG);
                request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

                return personCommandResponse;
            }

            personService.delete(id);

            final List<Person> users = personService.findByRole(USER);

            request.setAttribute(PERSON_ATTRIBUTE_NAME, PERSON_SUCCESSFULLY_DELETED_MSG);
            request.setAttribute(SELECT_PERSON_ATTRIBUTE_NAME, users);

            return personCommandResponse;
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ALL_FIELDS_MUST_BE_FILLED_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return personCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, EMPTY_ID_SENT_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return personCommandResponse;
        } catch (ServiceException | DaoException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, PERSON_CANNOT_BE_DELETED_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);

            return personCommandResponse;
        }
    }

    private Long getCheckedId(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final long id;

        if (request.getParameter(ID_PARAMETER_NAME) != null) {
            id = Long.parseLong(request.getParameter(ID_PARAMETER_NAME));
            return id;
        }

        throw new IncorrectEnteredDataException(ALL_FIELDS_MUST_BE_FILLED_MSG);
    }

}