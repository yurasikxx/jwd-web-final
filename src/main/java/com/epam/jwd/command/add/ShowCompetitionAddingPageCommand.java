package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Team;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_TEAM_ATTRIBUTE_NAME;

public class ShowCompetitionAddingPageCommand implements Command {

    private static final String COMPETITION_ADDING_OPERATION_MSG = "Competition adding operation";

    private static volatile ShowCompetitionAddingPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse;

    private ShowCompetitionAddingPageCommand() {
        this.competitionService = CompetitionService.getInstance();
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
        final List<Team> teams = competitionService.findAllTeams();

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_ADDING_OPERATION_MSG);
        request.setAttribute(SELECT_TEAM_ATTRIBUTE_NAME, teams);

        return competitionCommandResponse;
    }

}