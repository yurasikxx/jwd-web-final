package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.epam.jwd.dao.PersonDao.EMPTY_LIST_SIZE_VALUE;
import static com.epam.jwd.dao.PersonDao.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.dao.PersonDao.INITIAL_ID_VALUE;
import static com.epam.jwd.dao.PersonDao.INITIAL_INDEX_VALUE;

public class BetslipDao extends CommonDao<Betslip> implements BetslipBaseDao {

    private static final String SELECT_ALL_SQL_QUERY = "select bs.id, bs.c_id, bs.bt_id, bs.coefficient from %s;";
    private static final String FIND_ALL_SQL_QUERY = "select bs.id, bs.c_id,\n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name,\n" +
            "bt.bt_name, bs.coefficient \n" +
            "from %s\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "join bet_type bt on bs.bt_id = bt.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select bs.id, bs.c_id,\n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name,\n" +
            "bt.bt_name, bs.coefficient \n" +
            "from %s\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "betslip bs";
    private static final String BETSLIP_ID_COLUMN = "bs.id";
    private static final String COMPETITION_ID_COLUMN = "c_id";
    private static final String HOME_TEAM_ID_COLUMN = "t_home_id";
    private static final String HOME_TEAM_NAME_COLUMN = "th.t_name";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away_id";
    private static final String AWAY_TEAM_NAME_COLUMN = "ta.t_name";
    private static final String BET_TYPE_ID_COLUMN = "bt_id";
    private static final String BET_TYPE_NAME_COLUMN = "bt_name";
    private static final String BETSLIP_COEFFICIENT_COLUMN = "coefficient";
    private static final String SPORT_HOME_ID_COLUMN = "sh.id";
    private static final String SPORT_AWAY_ID_COLUMN = "sa.id";

    private static volatile BetslipDao instance;
    private final String findByCoefficientSql;

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
        this.findByCoefficientSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, BETSLIP_COEFFICIENT_COLUMN);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Betslip betslip) {
        try {
            final List<Betslip> betslips = this.findAll();
            if (betslips.size() == EMPTY_LIST_SIZE_VALUE) {
                resultSet.moveToInsertRow();
                resultSet.updateLong(BETSLIP_ID_COLUMN, INITIAL_ID_VALUE);
            } else {
                final AtomicLong competitionAmount = new AtomicLong(findAll().size());
                final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

                if (competitionAmount.get() == betslips.get(betslips.size() - INDEX_ROLLBACK_VALUE).getId()) {
                    long id = competitionAmount.incrementAndGet();

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(BETSLIP_ID_COLUMN, id);
                } else {
                    while (idCounter.get() == betslips.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId()) {
                        idCounter.incrementAndGet();
                    }

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(BETSLIP_ID_COLUMN, idCounter.get());
                }
            }

            resultSet.updateLong(COMPETITION_ID_COLUMN, betslip.getCompetition().getId());
            resultSet.updateLong(BET_TYPE_ID_COLUMN, betslip.getBetType().getId());
            resultSet.updateDouble(BETSLIP_COEFFICIENT_COLUMN, betslip.getCoefficient());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Betslip betslip) {
        try {
            long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == betslip.getId()) {
                resultSet.updateLong(COMPETITION_ID_COLUMN, betslip.getCompetition().getId());
                resultSet.updateLong(BET_TYPE_ID_COLUMN, betslip.getBetType().getId());
                resultSet.updateDouble(BETSLIP_COEFFICIENT_COLUMN, betslip.getCoefficient());
                resultSet.updateRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Betslip mapResultSet(ResultSet resultSet) throws SQLException {
        return new Betslip(resultSet.getLong(BETSLIP_ID_COLUMN),
                new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                        new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                                resultSet.getString(HOME_TEAM_NAME_COLUMN),
                                Sport.resolveSportById(resultSet.getLong(SPORT_HOME_ID_COLUMN))),
                        new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                                resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                                Sport.resolveSportById(resultSet.getLong(SPORT_AWAY_ID_COLUMN)))),
                BetType.resolveBetTypeByName(resultSet.getString(BET_TYPE_NAME_COLUMN)),
                resultSet.getDouble(BETSLIP_COEFFICIENT_COLUMN));
    }

    @Override
    public List<Betslip> findByCoefficient(double coefficient) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setDouble(INITIAL_INDEX_VALUE, coefficient),
                findByCoefficientSql);
    }

}