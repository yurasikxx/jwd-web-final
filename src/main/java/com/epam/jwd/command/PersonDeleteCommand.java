package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;
import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class PersonDeleteCommand implements Command {

    protected static final String ID_PARAMETER_NAME = "id";

    private static volatile PersonDeleteCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

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
        final List<Person> persons = personService.findAll();
        request.setAttribute(PERSON_ATTRIBUTE_NAME, persons);
        final String id = request.getParameter(ID_PARAMETER_NAME);

        try {
            personService.delete(Long.parseLong(id));
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
        }

        return personCommandResponse;
    }

}