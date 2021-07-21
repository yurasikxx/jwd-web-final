package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

public class PersonDeleteCommand implements Command {

    private static final String DELETING_PERSON_JSP_PATH = "/WEB-INF/jsp/deletingPerson.jsp";
    private static final String ID_PARAMETER_NAME = "id";

    private static volatile PersonDeleteCommand instance;
    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(DELETING_PERSON_JSP_PATH, false);

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
        final String id = request.getParameter(ID_PARAMETER_NAME);

        try {
            personService.delete(Long.parseLong(id));
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
        }

        return personCommandResponse;
    }

}
