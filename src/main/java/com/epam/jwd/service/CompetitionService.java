package com.epam.jwd.service;

import com.epam.jwd.dao.CompetitionBaseDao;
import com.epam.jwd.dao.CompetitionDao;
import com.epam.jwd.dao.TeamDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;

/**
 * A {@code CompetitionService} class is a competition service that is
 * an {@code CompetitionBaseService} interface implementation.
 *
 * @see BaseService
 * @see CompetitionBaseService
 */
public class CompetitionService implements CompetitionBaseService {

    private static final Logger LOGGER = LogManager.getLogger(CompetitionService.class);

    private static final String COMPETITION_WAS_SAVED_MSG = "Competition was saved";
    private static final String COMPETITION_WAS_NOT_SAVED_MSG = "Competition wasn't saved";
    private static final String COMPETITION_WAS_UPDATED_MSG = "Competition was updated";
    private static final String COMPETITION_WAS_NOT_UPDATED_MSG = "Competition wasn't updated";
    private static final String COMPETITION_WAS_FOUND_MSG = "Competition was found";
    private static final String COMPETITION_WAS_NOT_FOUND_MSG = "Competition wasn't found";
    private static final String COMPETITION_WAS_DELETED_MSG = "Competition was deleted";
    private static final String COMPETITION_WAS_NOT_DELETED_MSG = "Competition wasn't deleted";
    private static final String TEAM_WAS_FOUND_MSG = "Team was found";
    private static final String TEAM_WAS_NOT_FOUND_MSG = "Team wasn't found";
    private static final String COMPETITION_WERE_FOUND_MSG = "Competition were found";
    private static final String COMPETITIONS_WERE_NOT_FOUND_MSG = "Competitions weren't found";
    private static final String TEAMS_WERE_FOUND_MSG = "Teams were found";
    private static final String TEAMS_WERE_NOT_FOUND_MSG = "Teams weren't found";

    private static volatile CompetitionService instance;

    private final CompetitionBaseDao competitionDao;
    private final TeamDao teamDao;

    private CompetitionService() {
        this.competitionDao = CompetitionDao.getInstance();
        this.teamDao = TeamDao.getInstance();
    }

    public static CompetitionService getInstance() {
        if (instance == null) {
            synchronized (CompetitionService.class) {
                if (instance == null) {
                    instance = new CompetitionService();
                }
            }
        }

        return instance;
    }

    @Override
    public void save(Competition competition) {
        try {
            competitionDao.save(competition);
            LOGGER.info(COMPETITION_WAS_SAVED_MSG);
        } catch (DaoException e) {
            LOGGER.error(COMPETITION_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    public void update(Competition competition) {
        try {
            competitionDao.update(competition);
            LOGGER.info(COMPETITION_WAS_UPDATED_MSG);
        } catch (DaoException e) {
            LOGGER.error(COMPETITION_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    public List<Competition> findAll() {
        try {
            List<Competition> persons = competitionDao.findAll();
            LOGGER.info(COMPETITION_WERE_FOUND_MSG);

            return persons;
        } catch (DaoException e) {
            LOGGER.error(COMPETITIONS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public Competition findById(Long id) {
        Competition competition = null;

        try {
            final Optional<Competition> optional = competitionDao.findById(id);

            if (optional.isPresent()) {
                competition = optional.get();
            }

            LOGGER.info(COMPETITION_WAS_FOUND_MSG);
        } catch (DaoException e) {
            LOGGER.error(COMPETITION_WAS_NOT_FOUND_MSG);
        }

        return competition;
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            competitionDao.delete(id);
            LOGGER.info(COMPETITION_WAS_DELETED_MSG);
        } catch (DaoException e) {
            LOGGER.error(COMPETITION_WAS_NOT_DELETED_MSG);
            throw new ServiceException(COMPETITION_WAS_NOT_DELETED_MSG);
        }
    }

    @Override
    public boolean canBeDeleted(Long id) {
        final List<Competition> competitions = this.findAll();
        final Competition competition = this.findById(id);

        return competitions.contains(competition) && id > MIN_INDEX_VALUE;
    }

    @Override
    public boolean canSave(Competition competition) {
        final List<Competition> competitions = this.findAll();
        final List<Competition> theseCompetitions = new ArrayList<>();

        for (Competition iteratedCompetition : competitions) {
            theseCompetitions.add(new Competition(iteratedCompetition.getHome(), iteratedCompetition.getAway()));
        }

        return !theseCompetitions.contains(competition);
    }

    @Override
    public Team findTeamById(Long id) {
        Team team = null;

        try {
            final Optional<Team> optional = teamDao.findById(id);

            if (optional.isPresent()) {
                team = optional.get();
            }

            LOGGER.info(TEAM_WAS_FOUND_MSG);
        } catch (DaoException e) {
            LOGGER.error(TEAM_WAS_NOT_FOUND_MSG);
        }

        return team;
    }

    @Override
    public List<Competition> findBySportName(Sport sport) {
        try {
            final List<Competition> competitions = competitionDao.findBySportName(sport);
            LOGGER.info(COMPETITION_WERE_FOUND_MSG);

            return competitions;
        } catch (DaoException e) {
            LOGGER.error(COMPETITIONS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Team> findAllTeams() {
        try {
            final List<Team> teams = teamDao.findAll();
            LOGGER.info(TEAMS_WERE_FOUND_MSG);

            return teams;
        } catch (DaoException e) {
            LOGGER.info(TEAMS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

}