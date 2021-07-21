package com.epam.jwd.service;

import com.epam.jwd.dao.CompetitionBaseDao;
import com.epam.jwd.dao.CompetitionDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;

import java.util.List;

public class CompetitionService implements CompetitionBaseService {

    private static final String COMPETITION_WAS_NOT_FOUND_BY_GIVEN_ID_MSG = "Competition wasn't found by given id: %s";
    private static volatile CompetitionService instance;
    private final CompetitionBaseDao competitionDao;

    private CompetitionService() {
        this.competitionDao = CompetitionDao.getInstance();
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
    public Competition save(Competition competition) throws ServiceException, DaoException {
        competitionDao.save(competition);

        return competition;
    }

    @Override
    public boolean canSave(Competition competition) {
        return false;
    }

    @Override
    public void update(Competition entity) throws ServiceException, DaoException {

    }

    @Override
    public boolean canUpdate(Competition competition) {
        return false;
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
    public List<Competition> findBySportName(Sport sport) throws DaoException {
        return competitionDao.findBySportName(sport);
    }

}