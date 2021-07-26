package com.epam.jwd.service;

import com.epam.jwd.dao.CompetitionBaseDao;
import com.epam.jwd.dao.CompetitionDao;
import com.epam.jwd.dao.TeamBaseDao;
import com.epam.jwd.dao.TeamDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Team;

import java.util.ArrayList;
import java.util.List;

public class CompetitionService implements CompetitionBaseService {

    private static final String COMPETITION_WAS_NOT_FOUND_BY_GIVEN_ID_MSG = "Competition wasn't found by given id: %s";
    private static final String TEAM_WAS_NOT_FOUND_MSG = "Team with such id was not found: %s";
    private static volatile CompetitionService instance;
    private final CompetitionBaseDao competitionDao;
    private final TeamBaseDao teamDao;

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
    public void save(Competition competition) throws ServiceException, DaoException {
        competitionDao.save(competition);
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
    public void update(Competition competition) throws DaoException {
        competitionDao.update(competition);
    }

    @Override
    public List<Competition> findAll() {
        return competitionDao.findAll();
    }

    @Override
    public Competition findById(Long id) throws ServiceException, DaoException {
        return competitionDao.findById(id).
                orElseThrow(() -> new ServiceException(String.format(COMPETITION_WAS_NOT_FOUND_BY_GIVEN_ID_MSG, id)));
    }

    @Override
    public void delete(Long id) throws DaoException {
        competitionDao.delete(id);
    }

    @Override
    public boolean canBeDeleted(Long id) throws ServiceException, DaoException {
        return this.findAll().contains(this.findById(id)) && id > 0;
    }

    @Override
    public Team findTeamById(Long id) throws DaoException {
        return teamDao.findById(id).orElseThrow(() -> new DaoException(String.format(TEAM_WAS_NOT_FOUND_MSG, id)));
    }

    @Override
    public List<Team> findAllTeams() {
        return teamDao.findAll();
    }

}