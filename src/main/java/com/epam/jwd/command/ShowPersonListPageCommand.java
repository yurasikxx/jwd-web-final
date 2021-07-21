package com.epam.jwd.command;

import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

public class ShowPersonListPageCommand implements Command {

    protected static final String PERSON_ATTRIBUTE_NAME = "person";

    private static final String SHOWING_PERSON_LIST_JSP_PATH = "/WEB-INF/jsp/showingPersonList.jsp";

    private static volatile ShowPersonListPageCommand instance;
    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(SHOWING_PERSON_LIST_JSP_PATH, false);

    private ShowPersonListPageCommand() {
        this.personService = PersonService.getInstance();
    }

    public static ShowPersonListPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonListPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonListPageCommand();
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