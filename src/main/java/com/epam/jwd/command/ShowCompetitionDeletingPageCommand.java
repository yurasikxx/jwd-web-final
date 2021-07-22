package com.epam.jwd.command;

import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class ShowCompetitionDeletingPageCommand implements Command {

    private static final String ENTER_COMPETITION_ID_MESSAGE = "Enter competition ID which needs to be deleted";
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
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, ENTER_COMPETITION_ID_MESSAGE);
        return competitionDeletingPageResponse;
    }

}