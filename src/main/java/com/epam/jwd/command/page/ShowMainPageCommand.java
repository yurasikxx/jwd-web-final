package com.epam.jwd.command.page;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.BetslipBaseService;
import com.epam.jwd.service.BetslipService;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.epam.jwd.constant.Constant.MAIN_JSP_PATH;
import static com.epam.jwd.model.BetType.AWAY_TEAM_WILL_NOT_LOSE;
import static com.epam.jwd.model.BetType.AWAY_TEAM_WIN;
import static com.epam.jwd.model.BetType.DRAW;
import static com.epam.jwd.model.BetType.HOME_TEAM_WILL_NOT_LOSE;
import static com.epam.jwd.model.BetType.HOME_TEAM_WIN;
import static com.epam.jwd.model.BetType.NO_DRAW;
import static com.epam.jwd.model.Sport.BASKETBALL;
import static com.epam.jwd.model.Sport.FOOTBALL;
import static com.epam.jwd.model.Sport.HOCKEY;

/**
 * A {@code ShowMainPageCommand} class implements {@code Command}
 * interface and execute command that showing main page.
 *
 * @see Command
 */
public class ShowMainPageCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ShowMainPageCommand.class);

    private static final String EMPTY_COMPETITIONS_MESSAGE_KEY = "competition.empty";
    private static final String BASKETBALL_COMPETITION_ATTRIBUTE_NAME = "basketballCompetition";
    private static final String FOOTBALL_COMPETITION_ATTRIBUTE_NAME = "footballCompetition";
    private static final String HOCKEY_COMPETITION_ATTRIBUTE_NAME = "hockeyCompetition";
    private static final String EMPTY_BETSLIPS_MESSAGE_KEY = "betslip.empty";
    private static final String HOME_WIN_ATTRIBUTE_NAME = "homeWin";
    private static final String AWAY_WIN_ATTRIBUTE_NAME = "awayWin";
    private static final String DRAW_ATTRIBUTE_NAME = "draw";
    private static final String HOME_WILL_NOT_LOSE_ATTRIBUTE_NAME = "homeWillNotLose";
    private static final String AWAY_WILL_NOT_LOSE_ATTRIBUTE_NAME = "awayWillNotLose";
    private static final String NO_DRAW_ATTRIBUTE_NAME = "noDraw";

    private static volatile ShowMainPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BetslipBaseService betslipService;
    private final BaseCommandResponse mainPageResponse;

    private ShowMainPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.betslipService = BetslipService.getInstance();
        this.mainPageResponse = new CommandResponse(MAIN_JSP_PATH, false);
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
        showMainPage(request);
        return mainPageResponse;
    }

    private void showMainPage(BaseCommandRequest request) {
        try {
            final List<Competition> basketballCompetitions = competitionService.findBySportName(BASKETBALL);
            final List<Competition> footballCompetitions = competitionService.findBySportName(FOOTBALL);
            final List<Competition> hockeyCompetitions = competitionService.findBySportName(HOCKEY);
            final List<Betslip> homeWinBetslips = betslipService.findByBetType(HOME_TEAM_WIN);
            final List<Betslip> awayWinBetslips = betslipService.findByBetType(AWAY_TEAM_WIN);
            final List<Betslip> drawBetslips = betslipService.findByBetType(DRAW);
            final List<Betslip> homeWillNotLoseBetslips = betslipService.findByBetType(HOME_TEAM_WILL_NOT_LOSE);
            final List<Betslip> awayWillNotLoseBetslips = betslipService.findByBetType(AWAY_TEAM_WILL_NOT_LOSE);
            final List<Betslip> noDrawBetslips = betslipService.findByBetType(NO_DRAW);

            request.setAttribute(BASKETBALL_COMPETITION_ATTRIBUTE_NAME, basketballCompetitions);
            request.setAttribute(FOOTBALL_COMPETITION_ATTRIBUTE_NAME, footballCompetitions);
            request.setAttribute(HOCKEY_COMPETITION_ATTRIBUTE_NAME, hockeyCompetitions);
            request.setAttribute(HOME_WIN_ATTRIBUTE_NAME, homeWinBetslips);
            request.setAttribute(AWAY_WIN_ATTRIBUTE_NAME, awayWinBetslips);
            request.setAttribute(DRAW_ATTRIBUTE_NAME, drawBetslips);
            request.setAttribute(HOME_WILL_NOT_LOSE_ATTRIBUTE_NAME, homeWillNotLoseBetslips);
            request.setAttribute(AWAY_WILL_NOT_LOSE_ATTRIBUTE_NAME, awayWillNotLoseBetslips);
            request.setAttribute(NO_DRAW_ATTRIBUTE_NAME, noDrawBetslips);
        } catch (DaoException e) {
            LOGGER.info(messageManager.getString(EMPTY_COMPETITIONS_MESSAGE_KEY));
            LOGGER.info(messageManager.getString(EMPTY_BETSLIPS_MESSAGE_KEY));
        }
    }

}