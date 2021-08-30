package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
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
 * A {@code BetDao} class is a bet entity data access object that
 * is an implementation of abstract {@code CommonDao} class
 * and {@code BetBaseDao} interface.
 *
 * @see CommonDao
 * @see BetBaseDao
 */
public class BetDao extends CommonDao<Bet> implements BetBaseDao {

    private static final Logger LOGGER = LogManager.getLogger(BetDao.class);

    private final static String SELECT_ALL_SQL_QUERY = "select b.id, b.bs_id, b.bet_total, b.p_id from %s;";
    private final static String FIND_ALL_BET_SQL_QUERY = "select b.id, bs.c_id,\n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name,\n" +
            "b.bs_id, bt.bt_name, bs.coefficient, b.bet_total,\n" +
            "b.p_id, p.p_login, p.p_password, p.p_balance, p.pr_id, pr.pr_name \n" +
            "from %s\n" +
            "join betslip bs on b.bs_id = bs.id\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "join person p on b.p_id = p.id\n" +
            "join person_role pr on p.pr_id = pr.id;";
    private final static String FIND_BY_FIELD_SQL_QUERY = "select b.id, bs.c_id,\n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name,\n" +
            "b.bs_id, bt.bt_name, bs.coefficient, b.bet_total,\n" +
            "b.p_id, p.p_login, p.p_password, p.p_balance, p.pr_id, pr.pr_name \n" +
            "from %s\n" +
            "join betslip bs on b.bs_id = bs.id\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "join person p on b.p_id = p.id\n" +
            "join person_role pr on p.pr_id = pr.id\n" +
            "where %s = ?;";
    private final static String TABLE_NAME = "bet b";
    private static final String BET_ID_COLUMN = "b.id";
    private static final String BETSLIP_ID_COLUMN = "bs_id";
    private static final String COMPETITION_ID_COLUMN = "c_id";
    private static final String HOME_TEAM_ID_COLUMN = "t_home_id";
    private static final String HOME_TEAM_NAME_COLUMN = "th.t_name";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away_id";
    private static final String AWAY_TEAM_NAME_COLUMN = "ta.t_name";
    private static final String BET_TYPE_NAME_COLUMN = "bt_name";
    private static final String BETSLIP_COEFFICIENT_COLUMN = "coefficient";
    private static final String BET_TOTAL_COLUMN = "bet_total";
    private static final String PERSON_ID_COLUMN = "p_id";
    private static final String PERSON_LOGIN_COLUMN = "p_login";
    private static final String PERSON_PASSWORD_COLUMN = "p_password";
    private static final String PERSON_ROLE_NAME_COLUMN = "pr.pr_name";
    private static final String SPORT_HOME_ID_COLUMN = "sh.id";
    private static final String SPORT_AWAY_ID_COLUMN = "sa.id";
    private static final String PERSON_BALANCE_COLUMN = "p.p_balance";
    private static final String BET_WAS_SAVED_MSG = "Bet was saved";
    private static final String BET_WAS_NOT_SAVED_MSG = "Bet wasn't saved";
    private static final String BET_WAS_UPDATED_MSG = "Bet was updated";
    private static final String BET_WAS_NOT_UPDATED_MSG = "Bet wasn't updated";

    private static volatile BetDao instance;

    private final String findByCompetitionIdSql;

    public static BetDao getInstance() {
        if (instance == null) {
            synchronized (BetDao.class) {
                if (instance == null) {
                    instance = new BetDao();
                }
            }
        }

        return instance;
    }

    private BetDao() {
        super(TABLE_NAME, SELECT_ALL_SQL_QUERY, FIND_ALL_BET_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, BET_ID_COLUMN);
        this.findByCompetitionIdSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, COMPETITION_ID_COLUMN);
    }

    @Override
    public List<Bet> findByCompetitionId(Long id) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setLong(INITIAL_INDEX_VALUE, id),
                findByCompetitionIdSql);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Bet bet) {
        try {
            final List<Bet> bets = this.findAll();
            final AtomicLong betAmount = new AtomicLong(bets.size());
            final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

            setId(resultSet, bets, betAmount, idCounter);

            resultSet.updateLong(BETSLIP_ID_COLUMN, bet.getBetslip().getId());
            resultSet.updateInt(BET_TOTAL_COLUMN, bet.getBetTotal());
            resultSet.updateLong(PERSON_ID_COLUMN, bet.getPerson().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();

            LOGGER.info(BET_WAS_SAVED_MSG);
        } catch (SQLException | DaoException e) {
            LOGGER.error(BET_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Bet bet) {
        try {
            final long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == bet.getId()) {
                resultSet.updateLong(BETSLIP_ID_COLUMN, bet.getBetslip().getId());
                resultSet.updateInt(BET_TOTAL_COLUMN, bet.getBetTotal());
                resultSet.updateLong(PERSON_ID_COLUMN, bet.getPerson().getId());
                resultSet.updateRow();
            }

            LOGGER.info(BET_WAS_UPDATED_MSG);
        } catch (SQLException e) {
            LOGGER.error(BET_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    protected Bet mapResultSet(ResultSet resultSet) throws SQLException, UnknownEnumAttributeException {
        return new Bet(resultSet.getLong(BET_ID_COLUMN),
                new Betslip(resultSet.getLong(BETSLIP_ID_COLUMN),
                        new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                                new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                                        resultSet.getString(HOME_TEAM_NAME_COLUMN),
                                        Sport.resolveSportById(resultSet.getLong(SPORT_HOME_ID_COLUMN))),
                                new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                                        resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                                        Sport.resolveSportById(resultSet.getLong(SPORT_AWAY_ID_COLUMN)))),
                        BetType.resolveBetTypeByName(resultSet.getString(BET_TYPE_NAME_COLUMN)),
                        resultSet.getInt(BETSLIP_COEFFICIENT_COLUMN)),
                resultSet.getInt(BET_TOTAL_COLUMN),
                new Person(resultSet.getLong(PERSON_ID_COLUMN),
                        resultSet.getString(PERSON_LOGIN_COLUMN),
                        resultSet.getString(PERSON_PASSWORD_COLUMN),
                        resultSet.getInt(PERSON_BALANCE_COLUMN),
                        Role.resolveRoleByName(resultSet.getString(PERSON_ROLE_NAME_COLUMN))));
    }

    private void setId(ResultSet resultSet, List<Bet> bets, AtomicLong betAmount, AtomicLong idCounter) throws SQLException, DaoException {
        if (bets.size() == EMPTY_LIST_SIZE_VALUE) {
            setFirstId(resultSet);
        } else {
            setCustomId(resultSet, bets, betAmount, idCounter);
        }
    }

    private void setFirstId(ResultSet resultSet) throws SQLException {
        resultSet.moveToInsertRow();
        resultSet.updateLong(BET_ID_COLUMN, INITIAL_ID_VALUE);
    }

    private void setCustomId(ResultSet resultSet, List<Bet> bets, AtomicLong betAmount, AtomicLong idCounter) throws SQLException, DaoException {
        final Long lastBetId = bets.get(bets.size() - INDEX_ROLLBACK_VALUE).getId();

        if (lastBetId.equals(betAmount.get())) {
            final long id = betAmount.incrementAndGet();

            resultSet.moveToInsertRow();
            resultSet.updateLong(BET_ID_COLUMN, id);
        } else {
            while (getIntermediateId(bets, idCounter).equals(idCounter.get())) {
                idCounter.incrementAndGet();
            }

            checkExistingBet(bets, betAmount, idCounter);

            resultSet.moveToInsertRow();
            resultSet.updateLong(BET_ID_COLUMN, idCounter.get());
        }
    }


    private Long getIntermediateId(List<Bet> bets, AtomicLong idCounter) {
        return bets.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId();
    }

    private void checkExistingBet(List<Bet> bets, AtomicLong betAmount, AtomicLong idCounter) throws DaoException {
        final Optional<Bet> optionalBet = this.findById(idCounter.get());
        Bet bet = null;

        if (optionalBet.isPresent()) {
            bet = optionalBet.get();
        }

        if (bets.contains(bet)) {
            idCounter.set(betAmount.incrementAndGet());
        }
    }

}