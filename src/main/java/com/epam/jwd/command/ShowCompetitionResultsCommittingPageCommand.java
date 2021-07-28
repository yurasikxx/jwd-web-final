package com.epam.jwd.command;

import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;

public class ShowCompetitionResultsCommittingPageCommand implements Command {

    protected static final String COMPETITION_RESULTS_COMMITTING_OPERATION_MSG = "Competition results committing operation";
    protected static final String BET_HISTORY_ATTRIBUTE_NAME = "betHistory";
    protected static final String CALCULATING_JSP_PATH = "/WEB-INF/jsp/competitionResultsCommitting.jsp";
    protected static final String ENTER_COMPETITION_ID_MSG = "Enter ID of competition that need to commit: ";

    private static volatile ShowCompetitionResultsCommittingPageCommand instance;

    private final BaseCommandResponse betHistoryCommandResponse;

    private ShowCompetitionResultsCommittingPageCommand() {
        this.betHistoryCommandResponse = new CommandResponse(CALCULATING_JSP_PATH, false);
    }

    public static ShowCompetitionResultsCommittingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionResultsCommittingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionResultsCommittingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, COMPETITION_RESULTS_COMMITTING_OPERATION_MSG);
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, ENTER_COMPETITION_ID_MSG);

        return betHistoryCommandResponse;
    }

}