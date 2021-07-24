package com.epam.jwd.command;

import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;

public class ShowCompetitionAddingPageCommand implements Command {

    protected static final String ADDING_JSP_PATH = "/WEB-INF/jsp/adding.jsp";

    private static final String SPORT_ATTRIBUTE_NAME = "sport";
    private static final String HOME_TEAM_ATTRIBUTE_NAME = "homeTeam";
    private static final String AWAY_TEAM_ATTRIBUTE_NAME = "awayTeam";
    private static final String ENTER_SPORT_ID_MSG = "Enter sport ID: ";
    private static final String ENTER_HOME_TEAM_ID_MSG = "Enter home team ID: ";
    private static final String ENTER_AWAY_TEAM_ID_MSG = "Enter away team ID: ";
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
        request.setAttribute(SPORT_ATTRIBUTE_NAME, ENTER_SPORT_ID_MSG);
        request.setAttribute(HOME_TEAM_ATTRIBUTE_NAME, ENTER_HOME_TEAM_ID_MSG);
        request.setAttribute(AWAY_TEAM_ATTRIBUTE_NAME, ENTER_AWAY_TEAM_ID_MSG);

        return competitionCommandResponse;
    }

}