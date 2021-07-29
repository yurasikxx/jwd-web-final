package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ID_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MSG;

public class PersonDeletingCommand implements Command {

    private static final String WRONG_ENTERED_PERSON_DATA_MSG = "Person with such id doesn't exist or entered id is non-positive number";
    private static final String PERSON_CANNOT_BE_DELETED_MSG = "Person cannot be deleted while there is unplayed bet";
    private static final String PERSON_SUCCESSFULLY_DELETED_MSG = "Person successfully deleted";

    private static volatile PersonDeletingCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
    private final BaseCommandResponse personErrorCommandResponse = personCommandResponse;

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
        try {
            final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));

            if (!personService.canBeDeleted(id)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, WRONG_ENTERED_PERSON_DATA_MSG);
                request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
                return personErrorCommandResponse;
            }

            personService.delete(id);
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTRIBUTE_NAME, PERSON_CANNOT_BE_DELETED_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return personErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, EMPTY_ID_SENT_MSG);
            request.setAttribute(PERSON_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return personErrorCommandResponse;
        }

        request.setAttribute(PERSON_ATTRIBUTE_NAME, PERSON_SUCCESSFULLY_DELETED_MSG);

        return personCommandResponse;
    }

}