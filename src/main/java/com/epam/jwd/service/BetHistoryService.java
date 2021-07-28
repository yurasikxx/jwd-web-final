package com.epam.jwd.service;

import com.epam.jwd.dao.BetHistoryBaseDao;
import com.epam.jwd.dao.BetHistoryDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BetHistory;

import java.util.List;

public class BetHistoryService implements BetHistoryBaseService {

    private static volatile BetHistoryService instance;

    private final BetHistoryBaseDao betHistoryDao;

    private BetHistoryService() {
        this.betHistoryDao = BetHistoryDao.getInstance();
    }

    public static BetHistoryService getInstance() {
        if (instance == null) {
            synchronized (BetHistoryService.class) {
                if (instance == null) {
                    instance = new BetHistoryService();
                }
            }
        }

        return instance;
    }

    @Override
    public List<BetHistory> findAll() {
        return betHistoryDao.findAll();
    }

    @Override
    public void save(BetHistory betHistory) throws DaoException, ServiceException {
        betHistoryDao.save(betHistory);
    }

}