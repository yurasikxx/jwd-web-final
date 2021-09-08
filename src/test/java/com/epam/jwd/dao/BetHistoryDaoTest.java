package com.epam.jwd.dao;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Team;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.model.BetResult.LOSS;
import static com.epam.jwd.model.CompetitionResult.AWAY_TEAM;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class BetHistoryDaoTest {

    private final CompetitionBaseDao competitionDao = CompetitionDao.getInstance();
    private final BetBaseDao betDao = BetDao.getInstance();
    private final BetHistoryDao betHistoryDao = BetHistoryDao.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSave() throws DaoException {
        final Competition competition = competitionDao.findAll().get(MIN_INDEX_VALUE);
        final Bet bet = betDao.findByCompetitionId(competition.getId())
                .get(betDao.findByCompetitionId(competition.getId()).size() - INDEX_ROLLBACK_VALUE);
        final Team home = competition.getHome();
        final Team away = competition.getAway();
        final BetslipType betslipType = bet.getBetslip().getBetslipType();
        final BetType betType = bet.getBetType();
        final Betslip betslip = bet.getBetslip();
        final Integer coefficient = betslip.getCoefficient();
        final Integer betTotal = bet.getBetTotal();
        final String personLogin = bet.getPerson().getLogin();
        final BetHistory savedBet = betHistoryDao.save(
                new BetHistory(home, away, betslipType, betType, coefficient, betTotal, personLogin, AWAY_TEAM, LOSS));
        assertNotNull(savedBet);
    }

    @Test
    public void testFindAll() throws DaoException {
        final List<BetHistory> betHistoryList = betHistoryDao.findAll();
        assertNotNull(betHistoryList);
        assertFalse(betHistoryList.isEmpty());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}