package com.epam.jwd.service;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Team;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.epam.jwd.model.BetResult.LOSS;
import static com.epam.jwd.model.BetType.SINGLE;
import static com.epam.jwd.model.BetslipType.DRAW;
import static com.epam.jwd.model.CompetitionResult.AWAY_TEAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class BetHistoryServiceTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final int HOME_TEAM_INDEX = 5;
    private static final int AWAY_TEAM_INDEX = 7;
    private static final int COEFFICIENT = 5;
    private static final int BET_TOTAL = 100;
    private static final int BALANCE = 1000;

    private final PersonBaseService personService = PersonService.getInstance();
    private final CompetitionBaseService competitionService = CompetitionService.getInstance();
    private final BetslipBaseService betslipService = BetslipService.getInstance();
    private final BetBaseService betService = BetService.getInstance();
    private final BetHistoryBaseService betHistoryService = BetHistoryService.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSaveAndDelete() throws ServiceException {
        final List<Team> teams = competitionService.findAllTeams();
        final Team home = teams.get(HOME_TEAM_INDEX);
        final Team away = teams.get(AWAY_TEAM_INDEX);

        personService.save(new Person(LOGIN, PASSWORD, BALANCE));
        final Person person = personService.findById((long) personService.findAll().size());
        assertNotNull(person);
        assertEquals(personService.findAll().size(), person.getId().intValue());

        competitionService.save(new Competition(home, away));
        final Competition competition = competitionService.findById((long) competitionService.findAll().size());
        assertNotNull(competition);
        assertEquals(competitionService.findAll().size(), competition.getId().intValue());

        betslipService.save(new Betslip(competition, DRAW, COEFFICIENT));
        final Betslip betslip = betslipService.findById((long) betslipService.findAll().size());
        assertNotNull(betslip);
        assertEquals(betslipService.findAll().size(), betslip.getId().intValue());

        betService.save(new Bet(betslip, BET_TOTAL, SINGLE, person));
        final Bet bet = betService.findById((long) betService.findAll().size());
        assertNotNull(bet);
        assertEquals(betService.findAll().size(), bet.getId().intValue());

        betHistoryService.save(new BetHistory(
                competition.getHome(), competition.getAway(), betslip.getBetslipType(), bet.getBetType(),
                betslip.getCoefficient(), bet.getBetTotal(), person.getLogin(), AWAY_TEAM, LOSS)
        );

        betService.delete(bet.getId());
        betslipService.delete(betslip.getId());
        competitionService.delete(competition.getId());
        personService.delete(person.getId());
    }

    @Test
    public void testFindAll() {
        final List<BetHistory> bets = betHistoryService.findAll();
        assertNotNull(bets);
        assertFalse(bets.isEmpty());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}