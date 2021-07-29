package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;

public class ShowCompetitionDeletingPageCommand implements Command {

    private static final String COMPETITION_DELETING_OPERATION_MSG = "Competition deleting operation";

    private static volatile ShowCompetitionDeletingPageCommand instance;

    private final BaseCommandResponse competitionDeletingPageResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowCompetitionDeletingPageCommand() {
    }

    public static ShowCompetitionDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_DELETING_OPERATION_MSG);
        return competitionDeletingPageResponse;
    }

}