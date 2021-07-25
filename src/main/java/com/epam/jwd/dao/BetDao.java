package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
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

public class BetDao extends CommonDao<Bet> implements BetBaseDao {

    private final static String SELECT_ALL_SQL_QUERY = "select b.id, b.bs_id, b.bet_total, b.p_id from %s;";
    private final static String FIND_ALL_BET_SQL_QUERY = "select b.id, bs.c_id,\n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name,\n" +
            "b.bs_id, bt.bt_name, bs.coefficient, b.bet_total,\n" +
            "b.p_id, p.p_login, p.p_password, p.pr_id, pr.pr_name \n" +
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
            "b.p_id, p.p_login, p.p_password, p.pr_id, pr.pr_name \n" +
            "from %s\n" +
            "join betslip bs on b.bs_id = bs.id\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "join person p on b.p_id = p.id\n" +
            "join person_role pr on p.pr_id = pr.id;\n" +
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
    private static final String PERSON_ROLE_ID_COLUMN = "pr_id";
    private static final String SPORT_HOME_ID_COLUMN = "sh.id";
    private static final String SPORT_AWAY_ID_COLUMN = "sa.id";

    private static volatile BetDao instance;
    private final String findByTotalSql;

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
        this.findByTotalSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, BET_TOTAL_COLUMN);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Bet bet) {
        try {
            final List<Bet> bets = this.findAll();

            if (bets.size() == EMPTY_LIST_SIZE_VALUE) {
                resultSet.moveToInsertRow();
                resultSet.updateLong(BET_ID_COLUMN, INITIAL_ID_VALUE);
            } else {
                final AtomicLong betAmount = new AtomicLong(findAll().size());
                final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

                if (betAmount.get() == bets.get(bets.size() - INDEX_ROLLBACK_VALUE).getId()) {
                    long id = betAmount.incrementAndGet();

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(BET_ID_COLUMN, id);
                } else {
                    while (idCounter.get() == bets.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId()) {
                        idCounter.incrementAndGet();
                    }

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(BET_ID_COLUMN, idCounter.get());
                }
            }

            resultSet.updateLong(BETSLIP_ID_COLUMN, bet.getBetslip().getId());
            resultSet.updateInt(BET_TOTAL_COLUMN, bet.getBetTotal());
            resultSet.updateLong(PERSON_ID_COLUMN, bet.getPerson().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Bet bet) {
        try {
            long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == bet.getId()) {
                resultSet.updateLong(BETSLIP_ID_COLUMN, bet.getBetslip().getId());
                resultSet.updateInt(BET_TOTAL_COLUMN, bet.getBetTotal());
                resultSet.updateLong(PERSON_ID_COLUMN, bet.getPerson().getId());
                resultSet.updateRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Bet mapResultSet(ResultSet resultSet) throws SQLException {
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
                        resultSet.getDouble(BETSLIP_COEFFICIENT_COLUMN)),
                resultSet.getInt(BET_TOTAL_COLUMN),
                new Person(resultSet.getLong(PERSON_ID_COLUMN),
                        resultSet.getString(PERSON_LOGIN_COLUMN),
                        resultSet.getString(PERSON_PASSWORD_COLUMN),
                        Role.resolveRoleById(resultSet.getLong(PERSON_ROLE_ID_COLUMN))));
    }

    @Override
    public List<Bet> findByTotal(Integer betTotal) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setInt(INITIAL_INDEX_VALUE, betTotal),
                findByTotalSql);
    }

}