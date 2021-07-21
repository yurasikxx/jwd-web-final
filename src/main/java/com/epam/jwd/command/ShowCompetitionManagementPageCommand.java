package com.epam.jwd.command;

public class ShowCompetitionManagementPageCommand implements Command {

    private static final String COMPETITION_JSP_PATH = "/WEB-INF/jsp/competition.jsp";

    private static volatile ShowCompetitionManagementPageCommand instance;

    private final BaseCommandResponse competitionCommandResponse = new CommandResponse(COMPETITION_JSP_PATH, false);

    private ShowCompetitionManagementPageCommand() {
    }

    public static ShowCompetitionManagementPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionManagementPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionManagementPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return competitionCommandResponse;
    }

}