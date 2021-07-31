package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.REGISTER_JSP_PATH;

public class ShowRegistrationPageCommand implements Command {

    private static volatile ShowRegistrationPageCommand instance;

    private final BaseCommandResponse registrationCommandResponse;

    private ShowRegistrationPageCommand() {
        this.registrationCommandResponse = new CommandResponse(REGISTER_JSP_PATH, false);
    }

    public static ShowRegistrationPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowRegistrationPageCommand.class) {
                if (instance == null) {
                    instance = new ShowRegistrationPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return registrationCommandResponse;
    }

}