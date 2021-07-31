package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;

public class ShowCompetitionAddingPageCommand implements Command {

    private static final String COMPETITION_ADDING_OPERATION_MSG = "Competition adding operation";

    private static volatile ShowCompetitionAddingPageCommand instance;

    private final BaseCommandResponse competitionCommandResponse;

    private ShowCompetitionAddingPageCommand() {
        this.competitionCommandResponse = new CommandResponse(ADDING_JSP_PATH, false);
    }

    public static ShowCompetitionAddingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionAddingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionAddingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_ADDING_OPERATION_MSG);
        return competitionCommandResponse;
    }

}