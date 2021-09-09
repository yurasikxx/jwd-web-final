package com.epam.jwd.command.add;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Team;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.COMPETITION_JSP_PATH;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_MESSAGE_KEY;
import static com.epam.jwd.constant.Constant.HALF_DENOMINATOR;
import static com.epam.jwd.constant.Constant.INITIAL_ID_VALUE;
import static com.epam.jwd.constant.Constant.SUCCESS_JSP_PATH;
import static com.epam.jwd.model.Sport.BASKETBALL;
import static com.epam.jwd.model.Sport.FOOTBALL;
import static com.epam.jwd.model.Sport.HOCKEY;

public class RandomCompetitionsAddingCommand implements Command {

    private static final String COMPETITION_PRESENCE_MESSAGE_KEY = "competition.presence";
    private static final int MIN_RANDOM_VALUE = 0;
    private static final int MAX_RANDOM_VALUE = 9;

    private static volatile RandomCompetitionsAddingCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse successAddingCommandResponse;
    private final BaseCommandResponse errorAddingCommandResponse;

    private RandomCompetitionsAddingCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.successAddingCommandResponse = new CommandResponse(SUCCESS_JSP_PATH, true);
        this.errorAddingCommandResponse = new CommandResponse(COMPETITION_JSP_PATH, false);
    }

    public static RandomCompetitionsAddingCommand getInstance() {
        if (instance == null) {
            synchronized (RandomCompetitionsAddingCommand.class) {
                if (instance == null) {
                    instance = new RandomCompetitionsAddingCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Competition> competitions = competitionService.findAll();
        final List<Team> basketballTeams = competitionService.findTeamsBySportName(BASKETBALL);
        final List<Team> footballTeams = competitionService.findTeamsBySportName(FOOTBALL);
        final List<Team> hockeyTeams = competitionService.findTeamsBySportName(HOCKEY);

        if (checkCompetitionsPresence(request, competitions)) {
            return errorAddingCommandResponse;
        }

        if (createCompetitions(request, basketballTeams)) {
            return errorAddingCommandResponse;
        }

        if (createCompetitions(request, footballTeams)) {
            return errorAddingCommandResponse;
        }

        if (createCompetitions(request, hockeyTeams)) {
            return errorAddingCommandResponse;
        }

        return successAddingCommandResponse;
    }

    private boolean checkCompetitionsPresence(BaseCommandRequest request, List<Competition> competitions) {
        if (!competitions.isEmpty()) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_PRESENCE_MESSAGE_KEY));
            return true;
        }
        return false;
    }

    private boolean createCompetitions(BaseCommandRequest request, List<Team> teams) {
        for (int i = INITIAL_ID_VALUE; i < teams.size(); i += HALF_DENOMINATOR) {
            final int homeIndex = MIN_RANDOM_VALUE + (int) (Math.random() * MAX_RANDOM_VALUE);
            final int awayIndex = MIN_RANDOM_VALUE + (int) (Math.random() * MAX_RANDOM_VALUE);
            final Team home = teams.get(homeIndex);
            final Team away = teams.get(awayIndex);
            Competition competition = new Competition(home, away);

            if (!competitionService.canSave(competition) || homeIndex == awayIndex) {
                final int newHomeIndex = MIN_RANDOM_VALUE + (int) (Math.random() * MAX_RANDOM_VALUE);
                final int newAwayIndex = MIN_RANDOM_VALUE + (int) (Math.random() * MAX_RANDOM_VALUE);
                final Team newHome = teams.get(newHomeIndex);
                final Team newAway = teams.get(newAwayIndex);
                competition = new Competition(newHome, newAway);
            }

            try {
                competitionService.save(competition);
            } catch (ServiceException e) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(ERROR_MESSAGE_KEY));
                return true;
            }
        }
        return false;
    }

}