package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.EMPTY_USER_MESSAGE_KEY;

public class ShowPersonChangingPageCommand implements Command {

    private static final String PERSON_CHANGING_MESSAGE_KEY = "person.changing";

    private static volatile ShowPersonChangingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse personCommandResponse;

    private ShowPersonChangingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.personCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowPersonChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            final List<Person> users = personService.findByRole(Role.USER);

            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(PERSON_CHANGING_MESSAGE_KEY));
            request.setAttribute(SELECT_PERSON_ATTRIBUTE_NAME, users);
        } catch (DaoException e) {
            request.setAttribute(PERSON_ATTRIBUTE_NAME, messageManager.getString(PERSON_CHANGING_MESSAGE_KEY));
            request.setAttribute(SELECT_PERSON_ATTRIBUTE_NAME, messageManager.getString(EMPTY_USER_MESSAGE_KEY));
        }

        return personCommandResponse;
    }

}