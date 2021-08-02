package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

public class ShowPersonViewingPageCommand implements Command {

    private static volatile ShowPersonViewingPageCommand instance;

    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse = new CommandResponse(VIEWING_JSP_PATH, false);

    private ShowPersonViewingPageCommand() {
        this.personService = PersonService.getInstance();
    }

    public static ShowPersonViewingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonViewingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonViewingPageCommand();
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