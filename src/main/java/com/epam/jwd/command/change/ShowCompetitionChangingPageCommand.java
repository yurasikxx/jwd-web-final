package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Team;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_TEAM_ATTRIBUTE_NAME;

public class ShowCompetitionChangingPageCommand implements Command {

    private static final String COMPETITION_CHANGING_MESSAGE_KEY = "competition.changing";

    private static volatile ShowCompetitionChangingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse;

    private ShowCompetitionChangingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.competitionCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
    }

    public static ShowCompetitionChangingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionChangingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionChangingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Team> teams = competitionService.findAllTeams();
        final List<Competition> competitions = competitionService.findAll();

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_CHANGING_MESSAGE_KEY));
        request.setAttribute(SELECT_TEAM_ATTRIBUTE_NAME, teams);
        request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);

        return competitionCommandResponse;
    }

}