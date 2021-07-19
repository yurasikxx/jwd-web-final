package com.epam.jwd.command;

import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

public class ShowPersonPageCommand implements Command {

    private static final String PERSON_ATTRIBUTE_NAME = "person";
    private static final String PERSON_JSP_PATH = "/WEB-INF/jsp/person.jsp";

    private static volatile ShowPersonPageCommand instance;
    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(PERSON_JSP_PATH, false);

    private ShowPersonPageCommand() {
        this.personService = PersonService.getInstance();
    }

    public static ShowPersonPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Person> persons = personService.findAll();
        request.setAttribute(PERSON_ATTRIBUTE_NAME, persons);

        return personCommandResponse;
    }

}