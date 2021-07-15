package com.epam.jwd.service;

import com.epam.jwd.dao.CompetitionBaseDao;
import com.epam.jwd.dao.CompetitionDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;

import java.util.List;

public class CompetitionService implements CompetitionBaseService {

    private static volatile CompetitionService instance;
    private final CompetitionBaseDao competitionBaseDao;

    private CompetitionService() {
        this.competitionBaseDao = CompetitionDao.getInstance();
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
    public Competition save(Competition entity) throws DaoException {
        return null;
    }

    @Override
    public List<Competition> findAll() {
        return competitionBaseDao.findAll();
    }

    @Override
    public Competition findById(Long id) throws ServiceException, DaoException {
        return null;
    }

    @Override
    public void delete(Long id) throws DaoException {

    }

    @Override
    public List<Competition> findBySportName(Sport sport) throws DaoException {
        return null;
    }

}