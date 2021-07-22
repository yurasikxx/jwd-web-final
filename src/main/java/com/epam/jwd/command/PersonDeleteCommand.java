package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;
import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class PersonDeleteCommand implements Command {

    protected static final String ID_PARAMETER_NAME = "id";
    protected static final String TRY_AGAIN_MSG = "Try again";
    protected static final String EMPTY_ID_SENT_MSG = "ID was not entered";

    private static final String WRONG_ENTERED_PERSON_DATA_MSG = "Person with such id doesn't exist or entered id is non-positive number";
    private static final String PERSON_CANNOT_BE_DELETED_MSG = "Person cannot be deleted while there is unplayed bet";
    private static final String PERSON_SUCCESSFULLY_DELETED_MSG = "Person successfully deleted";

    private static volatile PersonDeleteCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
    private final BaseCommandResponse personErrorCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private PersonDeleteCommand() {
        this.personService = PersonService.getInstance();
    }

    public static PersonDeleteCommand getInstance() {
        if (instance == null) {
            synchronized (PersonDeleteCommand.class) {
                if (instance == null) {
                    instance = new PersonDeleteCommand();
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