package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.Competition;
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
 * A {@code CompetitionDao} class is a competition entity data access object that
 * is an implementation of abstract {@code CommonDao} class
 * and {@code CompetitionBaseDao} interface.
 *
 * @see CommonDao
 * @see CompetitionBaseDao
 */
public class CompetitionDao extends CommonDao<Competition> implements CompetitionBaseDao {

    private static final Logger LOGGER = LogManager.getLogger(CompetitionDao.class);

    private static final String SELECT_ALL_SQL_QUERY = "select c.id, t_home_id, t_away_id from %s;";
    private static final String FIND_ALL_SQL_QUERY = "select c.id, \n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name\n" +
            "from %s\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select c.id, \n" +
            "c.t_home_id, th.t_name, sh.id, sh.s_name,\n" +
            "c.t_away_id, ta.t_name, sa.id, sh.s_name\n" +
            "from %s\n" +
            "join team th on c.t_home_id = th.id\n" +
            "join sport sh on th.s_id = sh.id\n" +
            "join team ta on c.t_away_id = ta.id\n" +
            "join sport sa on th.s_id = sa.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "competition c";
    private static final String COMPETITION_ID_COLUMN = "c.id";
    private static final String SPORT_NAME_COLUMN = "sh.s_name";
    private static final String HOME_TEAM_ID_COLUMN = "t_home_id";
    private static final String HOME_TEAM_NAME_COLUMN = "th.t_name";
    private static final String AWAY_TEAM_ID_COLUMN = "t_away_id";
    private static final String AWAY_TEAM_NAME_COLUMN = "ta.t_name";
    private static final String SPORT_HOME_ID_COLUMN = "sh.id";
    private static final String SPORT_AWAY_ID_COLUMN = "sa.id";
    private static final String COMPETITION_WAS_SAVED_MSG = "Competition was saved";
    private static final String COMPETITION_WAS_NOT_SAVED_MSG = "Competition wasn't saved";
    private static final String COMPETITION_WAS_UPDATED_MSG = "Competition was updated";
    private static final String COMPETITION_WERE_NOT_UPDATED_MSG = "Competition weren't updated";

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

                    if (this.findById(idCounter.get()).isPresent()) {
                        if (competitions.contains(this.findById(idCounter.get()).get())) {
                            idCounter.set(competitionAmount.incrementAndGet());
                        }
                    }

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(COMPETITION_ID_COLUMN, idCounter.get());
                }
            }

            resultSet.updateLong(HOME_TEAM_ID_COLUMN, competition.getHome().getId());
            resultSet.updateLong(AWAY_TEAM_ID_COLUMN, competition.getAway().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();

            LOGGER.info(COMPETITION_WAS_SAVED_MSG);
        } catch (SQLException | DaoException e) {
            LOGGER.error(COMPETITION_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Competition competition) {
        try {
            long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == competition.getId()) {
                resultSet.updateLong(HOME_TEAM_ID_COLUMN, competition.getHome().getId());
                resultSet.updateLong(AWAY_TEAM_ID_COLUMN, competition.getAway().getId());
                resultSet.updateRow();
            }

            LOGGER.info(COMPETITION_WAS_UPDATED_MSG);
        } catch (SQLException e) {
            LOGGER.error(COMPETITION_WERE_NOT_UPDATED_MSG);
        }
    }

    @Override
    protected Competition mapResultSet(ResultSet resultSet) throws SQLException, UnknownEnumAttributeException {
        return new Competition(resultSet.getLong(COMPETITION_ID_COLUMN),
                new Team(resultSet.getLong(HOME_TEAM_ID_COLUMN),
                        resultSet.getString(HOME_TEAM_NAME_COLUMN),
                        Sport.resolveSportById(resultSet.getLong(SPORT_HOME_ID_COLUMN))),
                new Team(resultSet.getLong(AWAY_TEAM_ID_COLUMN),
                        resultSet.getString(AWAY_TEAM_NAME_COLUMN),
                        Sport.resolveSportById(resultSet.getLong(SPORT_AWAY_ID_COLUMN))));
    }

    @Override
    public List<Competition> findBySportName(Sport sport) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setString(INITIAL_INDEX_VALUE, sport.getName()),
                findBySportSql);
    }

}