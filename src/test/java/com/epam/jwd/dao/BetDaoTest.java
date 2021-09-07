package com.epam.jwd.dao;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Person;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.model.BetType.SINGLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BetDaoTest {

    private static final Integer BET_TOTAL = 500;
    private static final Long ID = 1L;
    private final BetslipBaseDao betslipDao = BetslipDao.getInstance();
    private final PersonBaseDao personDao = PersonDao.getInstance();
    private final BetBaseDao betDao = BetDao.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSavingAndDeleting() throws DaoException {
        final Betslip betslip = betslipDao.findAll().get(betslipDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(betslip);
        final Person person = personDao.findAll().get(personDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(person);
        final Bet savedBet = betDao.save(new Bet(betslip, BET_TOTAL, SINGLE, person));
        assertNotNull(savedBet);
        betDao.delete((long) betDao.findAll().size());
    }

    @Test
    public void testFindAll() {
        final List<Bet> bets = betDao.findAll();
        assertNotNull(bets);
        assertFalse(bets.isEmpty());
    }

    @Test
    public void testFindById() throws DaoException {
        final Betslip betslip = betslipDao.findAll().get(betslipDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(betslip);
        final Person person = personDao.findAll().get(personDao.findAll().size() - INDEX_ROLLBACK_VALUE);
        assertNotNull(person);
        final Bet savedBet = betDao.save(new Bet(betslip, BET_TOTAL, SINGLE, person));
        assertNotNull(savedBet);
        final Optional<Bet> betById = betDao.findById((long) betDao.findAll().size());
        assertNotNull(betById);
        assertTrue(betById.isPresent());
        assertEquals(betDao.findAll().size(), betById.get().getId().intValue());
        betDao.delete((long) betDao.findAll().size());
    }

    @Test
    public void findByCompetitionId() throws DaoException {
        final List<Bet> byCompetitionId = betDao.findByCompetitionId(ID);
        assertNotNull(byCompetitionId);
        assertFalse(byCompetitionId.isEmpty());
        assertEquals(ID, byCompetitionId.get(MIN_INDEX_VALUE).getBetslip().getCompetition().getId());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}