package com.epam.jwd.service;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Team;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class BetslipServiceTest {

    private static final int HOME_TEAM_INDEX = 5;
    private static final int AWAY_TEAM_INDEX = 7;
    private static final int COEFFICIENT = 5;

    private final CompetitionBaseService competitionService = CompetitionService.getInstance();
    private final BetslipBaseService betslipService = BetslipService.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSaveAndDelete() throws ServiceException {
        final List<Team> teams = competitionService.findAllTeams();
        final Team home = teams.get(HOME_TEAM_INDEX);
        final Team away = teams.get(AWAY_TEAM_INDEX);

        competitionService.save(new Competition(home, away));
        final Competition competition = competitionService.findById((long) competitionService.findAll().size());
        assertNotNull(competition);
        assertEquals(competitionService.findAll().size(), competition.getId().intValue());

        betslipService.save(new Betslip(competition, BetslipType.DRAW, COEFFICIENT));
        final Betslip betslip = betslipService.findById((long) betslipService.findAll().size());
        assertNotNull(betslip);
        assertEquals(betslipService.findAll().size(), betslip.getId().intValue());

        betslipService.delete(betslip.getId());
        competitionService.delete(competition.getId());
    }

    @Test
    public void testFindAll() {
        final List<Betslip> betslips = betslipService.findAll();
        assertNotNull(betslips);
        assertFalse(betslips.isEmpty());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}