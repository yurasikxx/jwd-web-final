package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.model.BetResult;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.CompetitionResult;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.epam.jwd.constant.Constant.EMPTY_LIST_SIZE_VALUE;
import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_ID_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;

/**
 * A {@code BetHistoryDao} class is a bet history entity data access object that
 * is an implementation of abstract {@code CommonDao} class.
 *
 * @see CommonDao
 */
public class BetHistoryDao extends CommonDao<BetHistory> {

    private static final Logger LOGGER = LogManager.getLogger(BetHistoryDao.class);

    private static final String SELECT_ALL_SQL_QUERY = "select bh.id, bh.t_home_id, bh.t_away_id, bh.bt_id,\n" +
            "bh.bh_coefficient, bh.bh_bet_total, bh.bh_p_login,\n" +
            "bh.cr_id, bh.br_id \n" +
            "from %s;";
    private static final String FIND_ALL_BET_HISTORY_SQL_QUERY = "select bh.id, bh.t_home_id, th.t_name, sh.id, sh.s_name, \n" +
            "bh.t_away_id, ta.t_name, sa.id, sh.s_name, bh.bt_id,\n" +
            "bh.bh_coefficient, bh.bh_bet_total, bh.bh_p_login,\n" +
            "bh.cr_id, cr.cr_name, bh.br_id, br.br_name \n" +
            "from %s\n" +
            "join team th on bh.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on bh.t_away_id = ta.id\n" +
            "join sport sa on ta.s_id = sa.id\n" +
            "join bet_type bt on bh.bt_id = bt.id\n" +
            "join competition_result cr on bh.cr_id = cr.id\n" +
            "join bet_result br on bh.br_id = br.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select bh.id, bh.t_home_id, th.t_name, sh.id, sh.s_name, \n" +
            "bh.t_away_id, ta.t_name, sa.id, sh.s_name, bh.bt_id,\n" +
            "bh.bh_coefficient, bh.bh_bet_total, bh.bh_p_login,\n" +
            "bh.cr_id, cr.cr_name, bh.br_id, br.br_name \n" +
            "from %s\n" +
            "join team th on bh.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on bh.t_away_id = ta.id\n" +
            "join sport sa on ta.s_id = sa.id\n" +
            "join bet_type bt on bh.bt_id = bt.id\n" +
            "join competition_result cr on bh.cr_id = cr.id\n" +
            "join bet_result br on bh.br_id = br.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "bet_history bh";
    private static final String BET_HISTORY_ID_COLUMN = "bh.id";
    private static final String HOME_TEAM_ID_COLUMN = "t_home_id";
    private static final String HOME_TEAM_NAME_COLUMN = "th.t_name";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away_id";
    private static final String AWAY_TEAM_NAME_COLUMN = "ta.t_name";
    private static final String SPORT_HOME_ID_COLUMN = "sh.id";
    private static final String SPORT_AWAY_ID_COLUMN = "sa.id";
    private static final String BET_TYPE_ID_COLUMN = "bh.bt_id";
    private static final String COEFFICIENT_COLUMN = "bh.bh_coefficient";
    private static final String BET_TOTAL_COLUMN = "bh.bh_bet_total";
    private static final String PERSON_LOGIN_COLUMN = "bh.bh_p_login";
    private static final String COMPETITION_RESULT_ID_COLUMN = "bh.cr_id";
    private static final String COMPETITION_RESULT_NAME_COLUMN = "cr.cr_name";
    private static final String BET_RESULT_ID_COLUMN = "bh.br_id";
    private static final String BET_WAS_SAVED_IN_BET_HISTORY_MSG = "Bet was saved in bet history";
    private static final String BET_WAS_NOT_SAVED_IN_BET_HISTORY_MSG = "Bet wasn't saved in bet history";

    private static volatile BetHistoryDao instance;

    public static BetHistoryDao getInstance() {
        if (instance == null) {
            synchronized (BetHistoryDao.class) {
                if (instance == null) {
                    instance = new BetHistoryDao();
                }
            }
        }

        return instance;
    }

    public BetHistoryDao() {
        super(TABLE_NAME, SELECT_ALL_SQL_QUERY, FIND_ALL_BET_HISTORY_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, BET_HISTORY_ID_COLUMN);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, BetHistory betHistory) {
        try {
            final List<BetHistory> bets = this.findAll();
            final AtomicLong betAmount = new AtomicLong(bets.size());
            final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

            setId(resultSet, bets, betAmount, idCounter);

            resultSet.updateLong(HOME_TEAM_ID_COLUMN, betHistory.getHome().getId());
            resultSet.updateLong(AWAY_TEAM_ID_COLUMN, betHistory.getAway().getId());
            resultSet.updateLong(BET_TYPE_ID_COLUMN, betHistory.getBetType().getId());
            resultSet.updateInt(COEFFICIENT_COLUMN, betHistory.getCoefficient());
            resultSet.updateInt(BET_TOTAL_COLUMN, betHistory.getBetTotal());
            resultSet.updateString(PERSON_LOGIN_COLUMN, betHistory.getPersonLogin());
            resultSet.updateLong(COMPETITION_RESULT_ID_COLUMN, betHistory.getCompetitionResult().getId());
            resultSet.updateLong(BET_RESULT_ID_COLUMN, betHistory.getBetResult().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();

            LOGGER.info(BET_WAS_SAVED_IN_BET_HISTORY_MSG);
        } catch (SQLException | DaoException e) {
            e.printStackTrace();
            LOGGER.error(BET_WAS_NOT_SAVED_IN_BET_HISTORY_MSG);
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, BetHistory betHistory) {
        try {
            final long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == betHistory.getId()) {
                resultSet.updateLong(HOME_TEAM_ID_COLUMN, betHistory.getHome().getId());
                resultSet.updateLong(AWAY_TEAM_ID_COLUMN, betHistory.getAway().getId());
                resultSet.updateLong(BET_TYPE_ID_COLUMN, betHistory.getBetType().getId());
                resultSet.updateInt(COEFFICIENT_COLUMN, betHistory.getCoefficient());
                resultSet.updateInt(BET_TOTAL_COLUMN, betHistory.getBetTotal());
                resultSet.updateString(PERSON_LOGIN_COLUMN, betHistory.getPersonLogin());
                resultSet.updateLong(COMPETITION_RESULT_ID_COLUMN, betHistory.getCompetitionResult().getId());
                resultSet.updateLong(BET_RESULT_ID_COLUMN, betHistory.getBetResult().getId());
                resultSet.updateRow();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected BetHistory mapResultSet(ResultSet resultSet) throws SQLException, UnknownEnumAttributeException {
        return new BetHistory(resultSet.getLong(BET_HISTORY_ID_COLUMN),
                new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                        resultSet.getString(HOME_TEAM_NAME_COLUMN),
                        Sport.resolveSportById(resultSet.getLong(SPORT_HOME_ID_COLUMN))),
                new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                        resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                        Sport.resolveSportById(resultSet.getLong(SPORT_AWAY_ID_COLUMN))),
                BetType.resolveBetTypeById(resultSet.getLong(BET_TYPE_ID_COLUMN)),
                resultSet.getInt(COEFFICIENT_COLUMN),
                resultSet.getInt(BET_TOTAL_COLUMN),
                resultSet.getString(PERSON_LOGIN_COLUMN),
                CompetitionResult.resolveCompetitionResultByName(resultSet.getString(COMPETITION_RESULT_NAME_COLUMN)),
                BetResult.resolveBetResultById(resultSet.getLong(BET_RESULT_ID_COLUMN)));
    }

    private void setId(ResultSet resultSet, List<BetHistory> bets, AtomicLong betAmount, AtomicLong idCounter) throws SQLException, DaoException {
        if (bets.size() == EMPTY_LIST_SIZE_VALUE) {
            setFirstId(resultSet);
        } else {
            setCustomId(resultSet, bets, betAmount, idCounter);
        }
    }

    private void setFirstId(ResultSet resultSet) throws SQLException {
        resultSet.moveToInsertRow();
        resultSet.updateLong(BET_HISTORY_ID_COLUMN, INITIAL_ID_VALUE);
    }

    private void setCustomId(ResultSet resultSet, List<BetHistory> bets, AtomicLong betAmount, AtomicLong idCounter) throws SQLException, DaoException {
        final Long getLastBet = bets.get(bets.size() - INDEX_ROLLBACK_VALUE).getId();

        if (getLastBet.equals(betAmount.get())) {
            final long id = betAmount.incrementAndGet();

            resultSet.moveToInsertRow();
            resultSet.updateLong(BET_HISTORY_ID_COLUMN, id);
        } else {
            while (getIntermediateId(bets, idCounter).equals(idCounter.get())) {
                idCounter.incrementAndGet();
            }

            checkExistingBet(bets, betAmount, idCounter);

            resultSet.moveToInsertRow();
            resultSet.updateLong(BET_HISTORY_ID_COLUMN, idCounter.get());
        }
    }

    private Long getIntermediateId(List<BetHistory> bets, AtomicLong idCounter) {
        return bets.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId();
    }

    private void checkExistingBet(List<BetHistory> betslips, AtomicLong betslipAmount, AtomicLong idCounter) throws DaoException {
        final Optional<BetHistory> optionalBetslip = this.findById(idCounter.get());
        BetHistory betslip = null;

        if (optionalBetslip.isPresent()) {
            betslip = optionalBetslip.get();
        }

        if (betslips.contains(betslip)) {
            idCounter.set(betslipAmount.incrementAndGet());
        }
    }

}