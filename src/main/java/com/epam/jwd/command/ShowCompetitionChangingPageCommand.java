package com.epam.jwd.command;

import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.AWAY_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ENTER_AWAY_TEAM_ID_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.ENTER_HOME_TEAM_ID_MSG;
import static com.epam.jwd.command.ShowCompetitionAddingPageCommand.HOME_TEAM_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.CHANGING_JSP_PATH;
import static com.epam.jwd.command.ShowPersonChangingPageCommand.ID_ATTRIBUTE_NAME;

public class ShowCompetitionChangingPageCommand implements Command {

    protected static final String ENTER_ID_OF_CHANGEABLE_COMPETITION_MSG = "Enter ID of changeable competition: ";

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
        request.setAttribute(ID_ATTRIBUTE_NAME, ENTER_ID_OF_CHANGEABLE_COMPETITION_MSG);
        request.setAttribute(HOME_TEAM_ATTRIBUTE_NAME, ENTER_HOME_TEAM_ID_MSG);
        request.setAttribute(AWAY_TEAM_ATTRIBUTE_NAME, ENTER_AWAY_TEAM_ID_MSG);

        return competitionCommandResponse;
    }

}