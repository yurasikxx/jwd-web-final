package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;

public class ShowCompetitionChangingPageCommand implements Command {

    private static final String COMPETITION_CHANGING_OPERATION_MSG = "Competition changing operation";

    private static volatile ShowCompetitionChangingPageCommand instance;

    private final BaseCommandResponse competitionCommandResponse;

    private ShowCompetitionChangingPageCommand() {
        this.competitionCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowCompetitionChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_CHANGING_OPERATION_MSG);
        return competitionCommandResponse;
    }

}