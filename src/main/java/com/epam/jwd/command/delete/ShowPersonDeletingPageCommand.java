package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Person;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;

import java.util.List;

import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_PERSON_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.EMPTY_USER_MESSAGE_KEY;
import static com.epam.jwd.model.Role.USER;

/**
 * A {@code ShowPersonDeletingPageCommand} class implements {@code Command}
 * interface and execute command that showing person deleting page.
 *
 * @see Command
 */
public class ShowPersonDeletingPageCommand implements Command {

    private static final String PERSON_DELETING_MESSAGE_KEY = "person.deleting";

    private static volatile ShowPersonDeletingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final PersonBaseService personService;
    private final BaseCommandResponse personDeletingPageResponse;

    private ShowPersonDeletingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.personService = PersonService.getInstance();
        this.personDeletingPageResponse = new CommandResponse(DELETING_JSP_PATH, false);
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
        try {
            final List<Person> users = personService.findByRole(USER);

            request.setAttribute(PERSON_ATTRIBUTE_NAME,
                    messageManager.getString(PERSON_DELETING_MESSAGE_KEY));
            request.setAttribute(SELECT_PERSON_ATTRIBUTE_NAME, users);

            return personDeletingPageResponse;
        } catch (DaoException e) {
            request.setAttribute(PERSON_ATTRIBUTE_NAME,
                    messageManager.getString(PERSON_DELETING_MESSAGE_KEY));
            request.setAttribute(SELECT_PERSON_ATTRIBUTE_NAME,
                    messageManager.getString(EMPTY_USER_MESSAGE_KEY));

            return personDeletingPageResponse;
        }
    }

}