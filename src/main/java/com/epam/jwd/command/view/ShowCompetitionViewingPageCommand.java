package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

public class ShowCompetitionViewingPageCommand implements Command {

    private static volatile ShowCompetitionViewingPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionPageResponse = new CommandResponse(VIEWING_JSP_PATH, false);

    private ShowCompetitionViewingPageCommand() {
        this.competitionService = CompetitionService.getInstance();
    }

    public static ShowCompetitionViewingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionViewingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionViewingPageCommand();
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