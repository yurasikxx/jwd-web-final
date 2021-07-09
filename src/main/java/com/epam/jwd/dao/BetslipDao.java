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

public class BetslipDao extends CommonDao<Betslip> implements BetslipBaseDao {

    private static final String FIND_ALL_SQL_QUERY = "select bs.id, bs.c_id, c.s_id, s.s_name, \n" +
            "c.t_home_id, th.t_name, th.t_country, th.t_rate, \n" +
            "c.t_away_id, ta.t_name, ta.t_country, ta.t_rate,\n" +
            "bt.bt_name, bs.coefficient\n" +
            "from %s\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join bet_type bt on bs.bt_id = bt.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select bs.id, bs.c_id, c.s_id, s.s_name, \n" +
            "c.t_home_id, th.t_name, th.t_country, th.t_rate, \n" +
            "c.t_away_id, ta.t_name, ta.t_country, ta.t_rate,\n" +
            "bt.bt_name, bs.coefficient\n" +
            "from %s\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "betslip bs";
    private static final String BETSLIP_ID_COLUMN = "bs.id";
    private static final String COMPETITION_ID_COLUMN = "c_id";
    private static final String SPORT_ID_COLUMN = "s_id";
    private static final String HOME_TEAM_ID_COLUMN = "t_home_id";
    private static final String HOME_TEAM_NAME_COLUMN = "th.t_name";
    private static final String HOME_TEAM_COUNTRY_COLUMN = "th.t_country";
    private static final String HOME_TEAM_RATE_COLUMN = "th.t_rate";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away_id";
    private static final String AWAY_TEAM_NAME_COLUMN = "ta.t_name";
    private static final String AWAY_TEAM_COUNTRY_COLUMN = "ta.t_country";
    private static final String AWAY_TEAM_RATE_COLUMN = "ta.t_rate";
    private static final String BET_TYPE_NAME_COLUMN = "bt_name";
    private static final String BETSLIP_COEFFICIENT_COLUMN = "coefficient";

    private final String findByCoefficientSql;

    public BetslipDao() {
        super(TABLE_NAME, FIND_ALL_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, BETSLIP_ID_COLUMN);
        this.findByCoefficientSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, BETSLIP_COEFFICIENT_COLUMN);
    }

    @Override
    protected Betslip mapResultSet(ResultSet resultSet) throws SQLException {
        return new Betslip(resultSet.getLong(BETSLIP_ID_COLUMN),
                new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                        Sport.resolveSportById(resultSet.getLong(SPORT_ID_COLUMN)),
                        new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                                resultSet.getString(HOME_TEAM_NAME_COLUMN),
                                resultSet.getString(HOME_TEAM_COUNTRY_COLUMN),
                                resultSet.getInt(HOME_TEAM_RATE_COLUMN)),
                        new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                                resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                                resultSet.getString(AWAY_TEAM_COUNTRY_COLUMN),
                                resultSet.getInt(AWAY_TEAM_RATE_COLUMN))),
                BetType.resolveBetTypeByName(resultSet.getString(BET_TYPE_NAME_COLUMN)),
                resultSet.getDouble(BETSLIP_COEFFICIENT_COLUMN));
    }

    @Override
    public List<Betslip> findByCoefficient(double coefficient) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setDouble(1, coefficient), findByCoefficientSql);
    }

}