package com.epam.jwd.dao;

import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;
import com.epam.jwd.pool.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BetDao extends CommonDao<Bet> implements BetBaseDao{

    private static final String BET_QUERY = "select b.id, b_name, c.id, c_name, s.id,\n" +
            " t_home.id, t_home.t_name, t_home.t_country, t_home.t_rate,\n" +
            " t_away.id, t_away.t_name, t_away.t_country, t_away.t_rate,\n" +
            " bt.id, p.id, p_name, p_login, p_password, pr_id from %s b\n" +
            "join competition c on b.c_id = c.id\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team t_home on c.t_home_id = t_home.id\n" +
            "join team t_away on c.t_away_id = t_away.id\n" +
            "join bet_type bt on b.bt_id = bt.id\n" +
            "join person p on b.p_id = p.id\n" +
            "where %s = ?;";
    private static final String BET_ID_COLUMN = "b.id";
    private static final String BET_NAME_COLUMN = "b_name";
    private static final String COMPETITION_ID_COLUMN = "c.id";
    private static final String COMPETITION_NAME_COLUMN = "c_name";
    private static final String SPORT_ID_COLUMN = "s.id";
    private static final String HOME_TEAM_ID_COLUMN = "t_home.id";
    private static final String HOME_TEAM_NAME_COLUMN = "t_home.t_name";
    private static final String HOME_TEAM_COUNTRY_COLUMN = "t_home.t_country";
    private static final String HOME_TEAM_RATE_COLUMN = "t_home.t_rate";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away.id";
    private static final String AWAY_TEAM_NAME_COLUMN = "t_away.t_name";
    private static final String AWAY_TEAM_COUNTRY_COLUMN = "t_away.t_country";
    private static final String AWAY_TEAM_RATE_COLUMN = "t_away.t_rate";
    private static final String BET_TYPE_ID_COLUMN = "bt.id";
    private static final String PERSON_ID_COLUMN = "p.id";
    private static final String PERSON_NAME_COLUMN = "p_name";
    private static final String PERSON_LOGIN_COLUMN = "p_login";
    private static final String PERSON_PASSWORD_COLUMN = "p_password";
    private static final String PERSON_ROLE_ID_COLUMN = "pr_id";

    private static final String TABLE_NAME = "bet";

    private final String findByNameSql;

    public BetDao() {
        super(TABLE_NAME);
        this.findByNameSql = String.format(BET_QUERY, TABLE_NAME, BET_NAME_COLUMN);
    }

    @Override
    public Optional<Bet> findBetByName(String name) {
        return takeFirstNotNull(findPreparedEntities(preparedStatement -> preparedStatement.setString(1, name), findByNameSql));
    }

    @Override
    protected Bet mapResultSet(ResultSet resultSet) throws SQLException {
        return new Bet(resultSet.getLong(BET_ID_COLUMN),
                resultSet.getString(BET_NAME_COLUMN),
                new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                        resultSet.getString(COMPETITION_NAME_COLUMN),
                        Sport.resolveSportById(resultSet.getLong(SPORT_ID_COLUMN)),
                        new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                                resultSet.getString(HOME_TEAM_NAME_COLUMN),
                                resultSet.getString(HOME_TEAM_COUNTRY_COLUMN),
                                resultSet.getInt(HOME_TEAM_RATE_COLUMN)),
                        new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                                resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                                resultSet.getString(AWAY_TEAM_COUNTRY_COLUMN),
                                resultSet.getInt(AWAY_TEAM_RATE_COLUMN))),
                BetType.resolveBetTypeById(resultSet.getLong(BET_TYPE_ID_COLUMN)),
                new Person(resultSet.getLong(PERSON_ID_COLUMN),
                        resultSet.getString(PERSON_NAME_COLUMN),
                        resultSet.getString(PERSON_LOGIN_COLUMN),
                        resultSet.getString(PERSON_PASSWORD_COLUMN),
                        Role.resolveRoleById(resultSet.getLong(PERSON_ROLE_ID_COLUMN))));
    }

}