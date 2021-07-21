package com.epam.jwd.command;

import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class ShowCompetitionDeletingPageCommand implements Command {

    private static volatile ShowCompetitionDeletingPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionDeletingPageResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private ShowCompetitionDeletingPageCommand() {
        this.competitionService = CompetitionService.getInstance();
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
        final List<Competition> competitions = competitionService.findAll();
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, competitions);

        return competitionDeletingPageResponse;
    }

}