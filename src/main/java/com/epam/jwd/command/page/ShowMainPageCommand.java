package com.epam.jwd.command.page;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.model.Sport.BASKETBALL;
import static com.epam.jwd.model.Sport.FOOTBALL;
import static com.epam.jwd.model.Sport.HOCKEY;

public class ShowMainPageCommand implements Command {

    private static final String MAIN_JSP_PATH = "/jsp/main.jsp";
    private static final String COMPETITIONS_WERE_NOT_FOUND_MSG = "Competitions weren't found";
    private static final String BASKETBALL_COMPETITION_ATTRIBUTE_NAME = "basketballCompetition";
    private static final String FOOTBALL_COMPETITION_ATTRIBUTE_NAME = "footballCompetition";
    private static final String HOCKEY_COMPETITION_ATTRIBUTE_NAME = "hockeyCompetition";

    private static volatile ShowMainPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse mainPageResponse = new CommandResponse(MAIN_JSP_PATH, false);

    private ShowMainPageCommand() {
        this.competitionService = CompetitionService.getInstance();
    }

    public static ShowMainPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowMainPageCommand.class) {
                if (instance == null) {
                    instance = new ShowMainPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            final List<Competition> basketballCompetitions = competitionService.findBySportName(BASKETBALL);
            final List<Competition> footballCompetitions = competitionService.findBySportName(FOOTBALL);
            final List<Competition> hockeyCompetitions = competitionService.findBySportName(HOCKEY);

            request.setAttribute(BASKETBALL_COMPETITION_ATTRIBUTE_NAME, basketballCompetitions);
            request.setAttribute(FOOTBALL_COMPETITION_ATTRIBUTE_NAME, footballCompetitions);
            request.setAttribute(HOCKEY_COMPETITION_ATTRIBUTE_NAME, hockeyCompetitions);

            return mainPageResponse;
        } catch (DaoException e) {
            request.setAttribute(BASKETBALL_COMPETITION_ATTRIBUTE_NAME, COMPETITIONS_WERE_NOT_FOUND_MSG);
            request.setAttribute(FOOTBALL_COMPETITION_ATTRIBUTE_NAME, COMPETITIONS_WERE_NOT_FOUND_MSG);
            request.setAttribute(HOCKEY_COMPETITION_ATTRIBUTE_NAME, COMPETITIONS_WERE_NOT_FOUND_MSG);

            return mainPageResponse;
        }
    }

}