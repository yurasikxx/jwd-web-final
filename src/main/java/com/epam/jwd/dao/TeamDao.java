package com.epam.jwd.dao;

import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.epam.jwd.constant.Constant.EMPTY_LIST_SIZE_VALUE;
import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_ID_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;

/**
 * A {@code TeamDao} class is a team entity data access object that
 * is an implementation of abstract {@code TeamDao} class.
 *
 * @see CommonDao
 */
public class TeamDao extends CommonDao<Team> {

    private static final Logger LOGGER = LogManager.getLogger(TeamDao.class);

    private static final String SELECT_ALL_SQL_QUERY = "select t.id, t.t_name, t.s_id, s.s_name from %s\n" +
            "join sport s on t.s_id = s.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select t.id, t.t_name, t.s_id, s.s_name from %s\n" +
            "join sport s on t.s_id = s.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "team t";
    private static final String TEAM_ID_COLUMN = "t.id";
    private static final String TEAM_NAME_COLUMN = "t_name";
    private static final String SPORT_NAME_COLUMN = "s.s_name";
    private static final String SPORT_ID_COLUMN = "t.s_id";
    private static final String TEAM_WAS_SAVED_MSG = "Team was saved";
    private static final String TEAM_WAS_NOT_SAVED_MSG = "Team wasn't saved";
    private static final String TEAM_WAS_UPDATED_MSG = "Team was updated";
    private static final String TEAM_WAS_NOT_UPDATED_MSG = "Team wasn't updated";

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
    protected void saveResultSet(ResultSet resultSet, Team team) {
        try {
            final List<Team> teams = this.findAll();
            final AtomicLong personAmount = new AtomicLong(teams.size());
            final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

            setId(resultSet, teams, personAmount, idCounter);

            resultSet.updateString(TEAM_NAME_COLUMN, team.getName());
            resultSet.updateLong(SPORT_ID_COLUMN, team.getSport().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();

            LOGGER.info(TEAM_WAS_SAVED_MSG);
        } catch (SQLException e) {
            LOGGER.error(TEAM_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Team team) {
        try {
            final long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == team.getId()) {
                resultSet.updateString(TEAM_NAME_COLUMN, team.getName());
                resultSet.updateLong(SPORT_ID_COLUMN, team.getSport().getId());
                resultSet.updateRow();
            }

            LOGGER.info(TEAM_WAS_UPDATED_MSG);
        } catch (SQLException e) {
            LOGGER.error(TEAM_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    protected Team mapResultSet(ResultSet resultSet) throws SQLException, UnknownEnumAttributeException {
        return new Team(resultSet.getLong(TEAM_ID_COLUMN),
                resultSet.getString(TEAM_NAME_COLUMN),
                Sport.resolveSportByName(resultSet.getString(SPORT_NAME_COLUMN)));
    }

    private void setId(ResultSet resultSet, List<Team> teams, AtomicLong personAmount, AtomicLong idCounter) throws SQLException {
        if (teams.size() == EMPTY_LIST_SIZE_VALUE) {
            setFirstId(resultSet);
        } else {
            setCustomId(resultSet, teams, personAmount, idCounter);
        }
    }

    private void setFirstId(ResultSet resultSet) throws SQLException {
        resultSet.moveToInsertRow();
        resultSet.updateLong(TEAM_ID_COLUMN, INITIAL_ID_VALUE);
    }

    private void setCustomId(ResultSet resultSet, List<Team> teams, AtomicLong personAmount, AtomicLong idCounter) throws SQLException {
        final Long lastTeamId = teams.get(teams.size() - INDEX_ROLLBACK_VALUE).getId();

        if (lastTeamId.equals(personAmount.get())) {
            final long id = personAmount.incrementAndGet();

            resultSet.moveToInsertRow();
            resultSet.updateLong(TEAM_ID_COLUMN, id);
        } else {
            while (getIntermediateId(teams, idCounter).equals(idCounter.get())) {
                idCounter.incrementAndGet();
            }

            resultSet.moveToInsertRow();
            resultSet.updateLong(TEAM_ID_COLUMN, idCounter.get());
        }
    }

    private Long getIntermediateId(List<Team> teams, AtomicLong idCounter) {
        return teams.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId();
    }

}