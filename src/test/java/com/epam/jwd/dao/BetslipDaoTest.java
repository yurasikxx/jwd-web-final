package com.epam.jwd.dao;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.model.BetType.DRAW;
import static com.epam.jwd.model.BetType.HOME_TEAM_WIN;
import static com.epam.jwd.model.BetType.NO_DRAW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BetslipDaoTest {

    private static final int COEFFICIENT = 5;
    private static final Long ID = 2L;

    private final CompetitionBaseDao competitionDao = CompetitionDao.getInstance();
    private final BetslipBaseDao betslipDao = BetslipDao.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSavingAndDeleting() throws DaoException {
        final List<Competition> competitions = competitionDao.findAll();
        assertNotNull(competitions);
        final Competition competition = competitions.get(competitions.size() - INDEX_ROLLBACK_VALUE);
        final Betslip saved = betslipDao.save(new Betslip(competition, DRAW, COEFFICIENT));
        assertNotNull(saved);
        final Betslip betslip = betslipDao.findAll().get(betslipDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(betslip);

        betslipDao.delete(betslip.getId());
    }

    @Test
    public void testFindAll() {
        final List<Betslip> betslips = betslipDao.findAll();
        assertNotNull(betslips);
    }

    @Test
    public void testFindById() throws DaoException {
        final List<Competition> competitions = competitionDao.findAll();
        assertNotNull(competitions);
        final Betslip saved = betslipDao.save(new Betslip(competitions.get(competitions.size() - INDEX_ROLLBACK_VALUE),
                DRAW, COEFFICIENT));
        assertNotNull(saved);
        final Betslip betslip = betslipDao.findAll().get(betslipDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(betslip);

        final Optional<Betslip> betslipById = betslipDao.findById(betslip.getId());
        assertTrue(betslipById.isPresent());
        assertEquals(betslip.getId(), betslipById.get().getId());

        betslipDao.delete(betslipById.get().getId());
    }

    @Test
    public void testUpdate() throws DaoException {
        final List<Competition> competitions = competitionDao.findAll();
        assertNotNull(competitions);
        final Betslip saved = betslipDao.save(new Betslip(competitions.get(competitions.size() - INDEX_ROLLBACK_VALUE),
                DRAW, COEFFICIENT));
        assertNotNull(saved);
        final Betslip betslip = betslipDao.findAll().get(betslipDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(betslip);

        final Betslip updatable = new Betslip(betslip.getId(), betslip.getCompetition(), NO_DRAW, COEFFICIENT);
        betslipDao.update(updatable);
        assertNotNull(updatable);
        assertEquals(NO_DRAW, updatable.getBetType());

        betslipDao.delete(updatable.getId());
    }

    @Test
    public void testFindByBetType() throws DaoException {
        final List<Betslip> betslips = betslipDao.findByBetType(HOME_TEAM_WIN);
        assertNotNull(betslips);
        assertFalse(betslips.isEmpty());
        assertEquals(HOME_TEAM_WIN, betslips.get(MIN_INDEX_VALUE).getBetType());
    }

    @Test
    public void testFindByCompetitionId() throws DaoException {
        final List<Betslip> betslips = betslipDao.findByCompetitionId(ID);
        assertNotNull(betslips);
        assertFalse(betslips.isEmpty());
        assertEquals(ID, betslips.get(MIN_INDEX_VALUE).getCompetition().getId());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}