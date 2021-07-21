package com.epam.jwd.command;

import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

import static com.epam.jwd.command.ShowPersonListPageCommand.PERSON_ATTRIBUTE_NAME;

public class ShowPersonDeletingPageCommand implements Command {

    protected static final String DELETING_JSP_PATH = "/WEB-INF/jsp/deleting.jsp";

    private static volatile ShowPersonDeletingPageCommand instance;
    private final PersonBaseService personService;
    private final BaseCommandResponse personDeletingPageResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowPersonDeletingPageCommand() {
        this.personService = PersonService.getInstance();
    }

    public static ShowPersonDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Person> persons = personService.findAll();
        request.setAttribute(PERSON_ATTRIBUTE_NAME, persons);

        return personDeletingPageResponse;
    }

}