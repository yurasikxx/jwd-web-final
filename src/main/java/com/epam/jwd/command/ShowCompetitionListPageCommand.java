package com.epam.jwd.command;

import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.command.ShowPersonListPageCommand.LIST_JSP_PATH;

public class ShowCompetitionListPageCommand implements Command {

    protected static final String COMPETITION_ATTRIBUTE_NAME = "competition";

    private static volatile ShowCompetitionListPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionPageResponse = new CommandResponse(LIST_JSP_PATH, false);

    private ShowCompetitionListPageCommand() {
        this.competitionService = CompetitionService.getInstance();
    }

    public static ShowCompetitionListPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionListPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionListPageCommand();
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