package com.epam.jwd.command;

import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.pool.ConnectionPoolManager;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

public class ShowCompetitionPageCommand implements Command {

    private static final String COMPETITION_ATTRIBUTE_NAME = "competition";
    private static final CommandResponse SHOW_COMPETITION_PAGE = new CommandResponse() {
        @Override
        public String getPath() {
            return "/WEB-INF/jsp/competition.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    private static volatile ShowCompetitionPageCommand instance;
    private final CompetitionService competitionService;

    private ShowCompetitionPageCommand() {
        competitionService = CompetitionService.getInstance();
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
    public CommandResponse execute(CommandRequest request) {
        final List<Competition> competitions = competitionService.findAll();
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, competitions);
        return SHOW_COMPETITION_PAGE;
    }

}