package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;
import com.epam.jwd.model.Competition;
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
 * A {@code BetslipDao} class is a betslip entity data access object that
 * is an implementation of abstract {@code CommonDao} class
 * and {@code BetslipBaseDao} interface.
 *
 * @see CommonDao
 * @see BetslipBaseDao
 */
public class BetslipDao extends CommonDao<Betslip> implements BetslipBaseDao {

    private static final Logger LOGGER = LogManager.getLogger(BetslipDao.class);

    private static final String SELECT_ALL_SQL_QUERY = "select bs.id, bs.c_id, bs.bst_id, bs.coefficient from %s;";
    private static final String FIND_ALL_SQL_QUERY = "select bs.id, bs.c_id,\n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name,\n" +
            "bst.bst_name, bs.coefficient \n" +
            "from %s\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "join betslip_type bst on bs.bst_id = bst.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select bs.id, bs.c_id,\n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name,\n" +
            "bst.bst_name, bs.coefficient \n" +
            "from %s\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "join betslip_type bst on bs.bst_id = bst.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "betslip bs";
    private static final String BETSLIP_ID_COLUMN = "bs.id";
    private static final String COMPETITION_ID_COLUMN = "c_id";
    private static final String HOME_TEAM_ID_COLUMN = "t_home_id";
    private static final String HOME_TEAM_NAME_COLUMN = "th.t_name";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away_id";
    private static final String AWAY_TEAM_NAME_COLUMN = "ta.t_name";
    private static final String BETSLIP_TYPE_ID_COLUMN = "bst_id";
    private static final String BETSLIP_TYPE_NAME_COLUMN = "bst_name";
    private static final String BETSLIP_COEFFICIENT_COLUMN = "coefficient";
    private static final String SPORT_HOME_ID_COLUMN = "sh.id";
    private static final String SPORT_AWAY_ID_COLUMN = "sa.id";
    private static final String BETSLIP_WAS_NOT_SAVED_MSG = "Betslip wasn't saved";
    private static final String BETSLIP_WAS_SAVED_MSG = "Betslip was saved";
    private static final String BETSLIP_WAS_UPDATED_MSG = "Betslip was updated";
    private static final String BETSLIP_WAS_NOT_UPDATED_MSG = "Betslip wasn't updated";

    private static volatile BetslipDao instance;

    private final String findByBetTypeSql;
    private final String findByCompetitionIdSql;

    public static BetslipDao getInstance() {
        if (instance == null) {
            synchronized (BetslipDao.class) {
                if (instance == null) {
                    instance = new BetslipDao();
                }
            }
        }

        return instance;
    }

    private BetslipDao() {
        super(TABLE_NAME, SELECT_ALL_SQL_QUERY, FIND_ALL_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, BETSLIP_ID_COLUMN);
        this.findByBetTypeSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, BETSLIP_TYPE_NAME_COLUMN);
        this.findByCompetitionIdSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, COMPETITION_ID_COLUMN);
    }

    @Override
    public List<Betslip> findByBetType(BetslipType betslipType) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement
                .setString(INITIAL_INDEX_VALUE, betslipType.getName()), findByBetTypeSql);
    }

    @Override
    public List<Betslip> findByCompetitionId(Long id) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement
                .setLong(INITIAL_INDEX_VALUE, id), findByCompetitionIdSql);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Betslip betslip) {
        try {
            final List<Betslip> betslips = this.findAll();
            final AtomicLong betslipAmount = new AtomicLong(betslips.size());
            final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

            setId(resultSet, betslips, betslipAmount, idCounter);

            resultSet.updateLong(COMPETITION_ID_COLUMN, betslip.getCompetition().getId());
            resultSet.updateLong(BETSLIP_TYPE_ID_COLUMN, betslip.getBetslipType().getId());
            resultSet.updateInt(BETSLIP_COEFFICIENT_COLUMN, betslip.getCoefficient());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();

            LOGGER.info(BETSLIP_WAS_SAVED_MSG);
        } catch (SQLException | DaoException e) {
            LOGGER.error(BETSLIP_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Betslip betslip) {
        try {
            final long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == betslip.getId()) {
                resultSet.updateLong(COMPETITION_ID_COLUMN, betslip.getCompetition().getId());
                resultSet.updateLong(BETSLIP_TYPE_ID_COLUMN, betslip.getBetslipType().getId());
                resultSet.updateInt(BETSLIP_COEFFICIENT_COLUMN, betslip.getCoefficient());
                resultSet.updateRow();
            }

            LOGGER.info(BETSLIP_WAS_UPDATED_MSG);
        } catch (SQLException e) {
            LOGGER.error(BETSLIP_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    protected Betslip mapResultSet(ResultSet resultSet) throws SQLException, UnknownEnumAttributeException {
        return new Betslip(resultSet.getLong(BETSLIP_ID_COLUMN),
                new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                        new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                                resultSet.getString(HOME_TEAM_NAME_COLUMN),
                                Sport.resolveSportById(resultSet.getLong(SPORT_HOME_ID_COLUMN))),
                        new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                                resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                                Sport.resolveSportById(resultSet.getLong(SPORT_AWAY_ID_COLUMN)))),
                BetslipType.resolveBetslipTypeByName(resultSet.getString(BETSLIP_TYPE_NAME_COLUMN)),
                resultSet.getInt(BETSLIP_COEFFICIENT_COLUMN));
    }

    private void setId(ResultSet resultSet, List<Betslip> betslips, AtomicLong betslipAmount, AtomicLong idCounter) throws SQLException, DaoException {
        if (betslips.size() == EMPTY_LIST_SIZE_VALUE) {
            setFirstId(resultSet);
        } else {
            setCustomId(resultSet, betslips, betslipAmount, idCounter);
        }
    }

    private void setFirstId(ResultSet resultSet) throws SQLException {
        resultSet.moveToInsertRow();
        resultSet.updateLong(BETSLIP_ID_COLUMN, INITIAL_ID_VALUE);
    }

    private void setCustomId(ResultSet resultSet, List<Betslip> betslips, AtomicLong betslipAmount, AtomicLong idCounter) throws SQLException, DaoException {
        final Long lastBetslipId = betslips.get(betslips.size() - INDEX_ROLLBACK_VALUE).getId();

        if (lastBetslipId.equals(betslipAmount.get())) {
            final long id = betslipAmount.incrementAndGet();

            resultSet.moveToInsertRow();
            resultSet.updateLong(BETSLIP_ID_COLUMN, id);
        } else {
            while (getIntermediateId(betslips, idCounter).equals(idCounter.get())) {
                idCounter.incrementAndGet();
            }

            checkExistingBetslip(betslips, betslipAmount, idCounter);

            resultSet.moveToInsertRow();
            resultSet.updateLong(BETSLIP_ID_COLUMN, idCounter.get());
        }
    }

    private Long getIntermediateId(List<Betslip> betslips, AtomicLong idCounter) {
        return betslips.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId();
    }

    private void checkExistingBetslip(List<Betslip> betslips, AtomicLong betslipAmount, AtomicLong idCounter) throws DaoException {
        final Optional<Betslip> optionalBetslip = this.findById(idCounter.get());
        Betslip betslip = null;

        if (optionalBetslip.isPresent()) {
            betslip = optionalBetslip.get();
        }

        if (betslips.contains(betslip)) {
            idCounter.set(betslipAmount.incrementAndGet());
        }
    }

}