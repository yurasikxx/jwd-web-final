package com.epam.jwd.command.delete;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;

public class ShowCompetitionDeletingPageCommand implements Command {

    private static final String COMPETITION_DELETING_OPERATION_MSG = "Competition deleting operation";

    private static volatile ShowCompetitionDeletingPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionDeletingPageResponse;

    private ShowCompetitionDeletingPageCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.competitionDeletingPageResponse = new CommandResponse(DELETING_JSP_PATH, false);
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

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_DELETING_OPERATION_MSG);
        request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);

        return competitionDeletingPageResponse;
    }

}