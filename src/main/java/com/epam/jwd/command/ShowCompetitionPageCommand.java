package com.epam.jwd.command;

import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

public class ShowCompetitionPageCommand implements Command {

    private static final String COMPETITION_ATTRIBUTE_NAME = "competition";
    private static final String COMPETITION_JSP_PATH = "/WEB-INF/jsp/competition.jsp";
    private static final boolean REDIRECT = false;

    private static volatile ShowCompetitionPageCommand instance;
    private final CompetitionService competitionService;
    private final BaseCommandResponse competitionPageResponse = new CommandResponse(COMPETITION_JSP_PATH, REDIRECT);

    private ShowCompetitionPageCommand() {
        this.competitionService = CompetitionService.getInstance();
    }

    public static ShowCompetitionPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Competition> competitions = competitionService.findAll();
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, competitions);
        return competitionPageResponse;
    }

}