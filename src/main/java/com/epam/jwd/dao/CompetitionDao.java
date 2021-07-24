package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
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

public class CompetitionDao extends CommonDao<Competition> implements CompetitionBaseDao {

    private static final String SELECT_ALL_SQL_QUERY = "select c.id, c.s_id, t_home_id, t_away_id from %s;";
    private static final String FIND_ALL_SQL_QUERY = "select c.id, c.s_id, s.s_name, \n" +
            "c.t_home_id, th.t_name, th.t_country, th.t_rate, \n" +
            "c.t_away_id, ta.t_name, ta.t_country, ta.t_rate\n" +
            "from %s\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join team ta on c.t_away_id = ta.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select c.id, c.s_id, s.s_name, \n" +
            "c.t_home_id, th.t_name, th.t_country, th.t_rate, \n" +
            "c.t_away_id, ta.t_name, ta.t_country, ta.t_rate\n" +
            "from %s\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "competition c";
    private static final String COMPETITION_ID_COLUMN = "c.id";
    private static final String SPORT_ID_COLUMN = "s_id";
    private static final String SPORT_NAME_COLUMN = "s_name";
    private static final String HOME_TEAM_ID_COLUMN = "t_home_id";
    private static final String HOME_TEAM_NAME_COLUMN = "th.t_name";
    private static final String HOME_TEAM_COUNTRY_COLUMN = "th.t_country";
    private static final String HOME_TEAM_RATE_COLUMN = "th.t_rate";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away_id";
    private static final String AWAY_TEAM_NAME_COLUMN = "ta.t_name";
    private static final String AWAY_TEAM_COUNTRY_COLUMN = "ta.t_country";
    private static final String AWAY_TEAM_RATE_COLUMN = "ta.t_rate";
    private static final String TEAM_DOES_NOT_EXIST_MSG = "Team with given id doesn't exist: %s";

    private static volatile CompetitionDao instance;
    private final String findBySportSql;

    public static CompetitionDao getInstance() {
        if (instance == null) {
            synchronized (CompetitionDao.class) {
                if (instance == null) {
                    instance = new CompetitionDao();
                }
            }
        }

        return instance;
    }

    private CompetitionDao() {
        super(TABLE_NAME, SELECT_ALL_SQL_QUERY, FIND_ALL_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, COMPETITION_ID_COLUMN);
        this.findBySportSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, SPORT_NAME_COLUMN);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Competition competition) {
        try {
            final List<Competition> competitions = this.findAll();
            if (competitions.size() == EMPTY_LIST_SIZE_VALUE) {
                resultSet.moveToInsertRow();
                resultSet.updateLong(COMPETITION_ID_COLUMN, INITIAL_ID_VALUE);
            } else {
                final AtomicLong competitionAmount = new AtomicLong(findAll().size());
                final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

                if (competitionAmount.get() == competitions.get(competitions.size() - INDEX_ROLLBACK_VALUE).getId()) {
                    long id = competitionAmount.incrementAndGet();

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(COMPETITION_ID_COLUMN, id);
                } else {
                    while (idCounter.get() == competitions.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId()) {
                        idCounter.incrementAndGet();
                    }

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(COMPETITION_ID_COLUMN, idCounter.get());
                }
            }

            resultSet.updateLong(SPORT_ID_COLUMN, competition.getSport().getId());
            resultSet.updateLong(HOME_TEAM_ID_COLUMN, competition.getHome().getId());
            resultSet.updateLong(AWAY_TEAM_ID_COLUMN, competition.getAway().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Competition competition) {
        try {
            long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == competition.getId()) {
                resultSet.updateLong(SPORT_ID_COLUMN, competition.getSport().getId());
                resultSet.updateLong(HOME_TEAM_ID_COLUMN, competition.getHome().getId());
                resultSet.updateLong(AWAY_TEAM_ID_COLUMN, competition.getAway().getId());
                resultSet.updateRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Competition mapResultSet(ResultSet resultSet) throws SQLException {
        return new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                Sport.resolveSportById(resultSet.getLong(SPORT_ID_COLUMN)),
                new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                        resultSet.getString(HOME_TEAM_NAME_COLUMN),
                        resultSet.getString(HOME_TEAM_COUNTRY_COLUMN),
                        resultSet.getInt(HOME_TEAM_RATE_COLUMN)),
                new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                        resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                        resultSet.getString(AWAY_TEAM_COUNTRY_COLUMN),
                        resultSet.getInt(AWAY_TEAM_RATE_COLUMN)));
    }

    @Override
    public List<Competition> findBySportName(Sport sport) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setString(INITIAL_INDEX_VALUE, sport.getName()),
                findBySportSql);
    }

}