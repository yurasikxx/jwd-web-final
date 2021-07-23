package com.epam.jwd.dao;

import com.epam.jwd.exception.BusinessValidationException;
import com.epam.jwd.model.Team;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamDao extends CommonDao<Team> implements BaseDao<Team> {

    private static final String SELECT_ALL_SQL_QUERY = "select t.id, t_name, t_country, t_rate from %s;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select t.id, t_name, t_country, t_rate from %s\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "team t";
    private static final String TEAM_ID_COLUMN = "t.id";
    private static final String TEAM_NAME_COLUMN = "t_name";
    private static final String TEAM_COUNTRY_COLUMN = "t_country";
    private static final String TEAM_RATE_COLUMN = "t_rate";

    private static volatile TeamDao instance;

    public static TeamDao getInstance() {
        if (instance == null) {
            synchronized (TeamDao.class) {
                if (instance == null) {
                    instance = new TeamDao();
                }
            }
        }

        return instance;
    }

    private TeamDao() {
        super(TABLE_NAME, SELECT_ALL_SQL_QUERY, SELECT_ALL_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, TEAM_ID_COLUMN);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Team entity) throws BusinessValidationException {

    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Team entity) throws BusinessValidationException {

    }

    @Override
    protected Team mapResultSet(ResultSet resultSet) throws SQLException {
        return new Team(resultSet.getLong(TEAM_ID_COLUMN),
                resultSet.getString(TEAM_NAME_COLUMN),
                resultSet.getString(TEAM_COUNTRY_COLUMN),
                resultSet.getInt(TEAM_RATE_COLUMN));
    }

}