package com.epam.jwd.dao;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Team;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.model.Sport.BASKETBALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CompetitionDaoTest {

    private final TeamDao teamDao = TeamDao.getInstance();
    private final CompetitionBaseDao competitionDao = CompetitionDao.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSavingAndDeleting() throws DaoException {
        final List<Team> teams = teamDao.findAll();
        final Competition saved = competitionDao.save(new Competition(
                teams.get(MIN_INDEX_VALUE), teams.get(INITIAL_INDEX_VALUE)
        ));
        assertNotNull(saved);

        final Competition competition = competitionDao.findAll()
                .get(competitionDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(competition);

        competitionDao.delete(competition.getId());
    }

    @Test
    public void update() throws DaoException {
        final List<Team> teams = teamDao.findAll();
        final Competition saved = competitionDao.save(new Competition(
                teams.get(MIN_INDEX_VALUE), teams.get(INITIAL_INDEX_VALUE)
        ));
        assertNotNull(saved);

        final Competition competition = competitionDao.findAll()
                .get(competitionDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(competition);

        final Long id = competition.getId();
        final Team home = competition.getHome();
        final Team away = teams.get(INITIAL_INDEX_VALUE + INITIAL_INDEX_VALUE);
        final Competition updatable = new Competition(id, home, away);
        competitionDao.update(updatable);
        assertNotNull(updatable);
        assertEquals(away, updatable.getAway());

        competitionDao.delete(updatable.getId());
    }

    @Test
    public void testFindAll() throws DaoException {
        final List<Competition> competitions = competitionDao.findAll();
        assertNotNull(competitions);
    }

    @Test
    public void testFindById() throws DaoException {
        final List<Team> teams = teamDao.findAll();
        final Competition saved = competitionDao.save(new Competition(
                teams.get(MIN_INDEX_VALUE), teams.get(INITIAL_INDEX_VALUE)
        ));
        assertNotNull(saved);

        final Competition competition = competitionDao.findAll()
                .get(competitionDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(competition);

        final Optional<Competition> competitionById = competitionDao.findById(competition.getId());
        assertTrue(competitionById.isPresent());
        assertEquals(competition.getId(), competitionById.get().getId());

        competitionDao.delete(competitionById.get().getId());
    }

    @Test
    public void findBySportName() throws DaoException {
        final List<Competition> competitions = competitionDao.findBySportName(BASKETBALL);
        assertNotNull(competitions);
        assertFalse(competitions.isEmpty());
        assertEquals(BASKETBALL, competitions.get(MIN_INDEX_VALUE).getAway().getSport());
        assertEquals(BASKETBALL, competitions.get(MIN_INDEX_VALUE).getHome().getSport());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}