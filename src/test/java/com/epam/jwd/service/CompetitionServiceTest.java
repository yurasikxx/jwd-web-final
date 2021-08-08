package com.epam.jwd.service;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
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

public class CompetitionServiceTest {

    private static final int HOME_TEAM_INDEX = 5;
    private static final int AWAY_TEAM_INDEX = 7;

    private final CompetitionBaseService competitionService = CompetitionService.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSaveAndDelete() throws ServiceException, DaoException {
        final List<Team> teams = competitionService.findAllTeams();
        final Team home = teams.get(HOME_TEAM_INDEX);
        final Team away = teams.get(AWAY_TEAM_INDEX);
        competitionService.save(new Competition(home, away));
        final Competition competition = competitionService.findById((long) competitionService.findAll().size());
        assertNotNull(competition);
        assertEquals(competitionService.findAll().size(), competition.getId().intValue());
        competitionService.delete(competition.getId());
    }

    @Test
    public void testFindAll() {
        final List<Competition> competitions = competitionService.findAll();
        assertNotNull(competitions);
        assertFalse(competitions.isEmpty());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}