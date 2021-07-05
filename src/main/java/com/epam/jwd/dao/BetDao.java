package com.epam.jwd.dao;

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

public class BetDao extends CommonDao<Bet> implements BetBaseDao {

    private static final String BET_QUERY = "select b.id, b.bet_total, bs.id, bs.coefficient, c.id, s.id, s.s_name,\n" +
            " t_home.id, t_home.t_name, t_home.t_country, t_home.t_rate,\n" +
            " t_away.id, t_away.t_name, t_away.t_country, t_away.t_rate,\n" +
            " bt_name, p.id, p.p_login, p.p_password, pr.pr_name from %s b\n" +
            "join betslip bs on b.bs_id = bs.id\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team t_home on c.t_home_id = t_home.id\n" +
            "join team t_away on c.t_away_id = t_away.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "join person p on b.p_id = p.id\n" +
            "join person_role pr on p.pr_id = pr.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "bet";
    private static final String BET_ID_COLUMN = "b.id";
    private static final String BET_TOTAL_COLUMN = "b.bet_total";
    private static final String BETSLIP_ID_COLUMN = "bs.id";
    private static final String BETSLIP_COEFFICIENT_COLUMN = "bs.coefficient";
    private static final String COMPETITION_ID_COLUMN = "c.id";
    private static final String SPORT_NAME_COLUMN = "s.s_name";
    private static final String HOME_TEAM_ID_COLUMN = "t_home.id";
    private static final String HOME_TEAM_NAME_COLUMN = "t_home.t_name";
    private static final String HOME_TEAM_COUNTRY_COLUMN = "t_home.t_country";
    private static final String HOME_TEAM_RATE_COLUMN = "t_home.t_rate";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away.id";
    private static final String AWAY_TEAM_NAME_COLUMN = "t_away.t_name";
    private static final String AWAY_TEAM_COUNTRY_COLUMN = "t_away.t_country";
    private static final String AWAY_TEAM_RATE_COLUMN = "t_away.t_rate";
    private static final String BET_TYPE_NAME_COLUMN = "bt.bt_name";
    private static final String PERSON_ID_COLUMN = "p.id";
    private static final String PERSON_LOGIN_COLUMN = "p.p_login";
    private static final String PERSON_PASSWORD_COLUMN = "p.p_password";
    private static final String PERSON_ROLE_NAME_COLUMN = "pr.pr_name";

    private final String findByNameSql;

    public BetDao() {
        super(TABLE_NAME);
        this.findByNameSql = String.format(BET_QUERY, TABLE_NAME, "b.bet_total");
    }

    @Override
    public List<Bet> findBetByTotal(Integer betTotal) {
        return findPreparedEntities(preparedStatement -> preparedStatement.setInt(1, betTotal), findByNameSql);
    }

    @Override
    protected Bet mapResultSet(ResultSet resultSet) throws SQLException {
        return new Bet(resultSet.getLong(BET_ID_COLUMN),
                resultSet.getInt(BET_TOTAL_COLUMN),
                new Betslip(resultSet.getLong(BETSLIP_ID_COLUMN),
                        resultSet.getDouble(BETSLIP_COEFFICIENT_COLUMN),
                        new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                                Sport.resolveSportByName(resultSet.getString(SPORT_NAME_COLUMN)),
                                new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                                        resultSet.getString(HOME_TEAM_NAME_COLUMN),
                                        resultSet.getString(HOME_TEAM_COUNTRY_COLUMN),
                                        resultSet.getInt(HOME_TEAM_RATE_COLUMN)),
                                new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                                        resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                                        resultSet.getString(AWAY_TEAM_COUNTRY_COLUMN),
                                        resultSet.getInt(AWAY_TEAM_RATE_COLUMN))),
                        BetType.resolveBetTypeByName(resultSet.getString(BET_TYPE_NAME_COLUMN))),
                new Person(resultSet.getLong(PERSON_ID_COLUMN),
                        resultSet.getString(PERSON_LOGIN_COLUMN),
                        resultSet.getString(PERSON_PASSWORD_COLUMN),
                        Role.resolveRoleByName(resultSet.getString(PERSON_ROLE_NAME_COLUMN))));
    }

}
