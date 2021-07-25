package com.epam.jwd.dao;

import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TeamDao extends CommonDao<Team> implements TeamBaseDao {

    private static final String SELECT_ALL_SQL_QUERY = "select t.id, t.t_name, t.s_id, s.s_name from %s\n" +
            "join sport s on t.s_id = s.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select t.id, t.t_name, t.s_id, s.s_name from %s\n" +
            "join sport s on t.s_id = s.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "team t";
    private static final String TEAM_ID_COLUMN = "t.id";
    private static final String TEAM_NAME_COLUMN = "t_name";
    private static final String SPORT_NAME_COLUMN = "s.s_name";

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
    protected void saveResultSet(ResultSet resultSet, Team entity) {

    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Team entity) {

    }

    @Override
    protected Team mapResultSet(ResultSet resultSet) throws SQLException {
        return new Team(resultSet.getLong(TEAM_ID_COLUMN),
                resultSet.getString(TEAM_NAME_COLUMN),
                Sport.resolveSportByName(resultSet.getString(SPORT_NAME_COLUMN)));
    }


    @Override
    public List<Team> findByCountry(String country) {
        return null;
    }

    @Override
    public List<Team> findByRate(int rate) {
        return null;
    }

}