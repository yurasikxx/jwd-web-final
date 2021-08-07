package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.Team;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.ADDING_JSP_PATH;
import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.SELECT_TEAM_ATTRIBUTE_NAME;

/**
 * A {@code ShowCompetitionAddingPageCommand} class implements {@code Command}
 * interface and execute command that showing competition adding page.
 *
 * @see Command
 */
public class ShowCompetitionAddingPageCommand implements Command {

    private static final String COMPETITION_ADDING_MESSAGE_KEY = "competition.adding";

    private static volatile ShowCompetitionAddingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse;

    private ShowCompetitionAddingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
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
        final List<Team> teams = competitionService.findAllTeams()
                .stream()
                .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_ADDING_MESSAGE_KEY));
        request.setAttribute(SELECT_TEAM_ATTRIBUTE_NAME, teams);

        return competitionCommandResponse;
    }

}