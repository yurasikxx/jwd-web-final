package com.epam.jwd.service;

import com.epam.jwd.dao.BetslipBaseDao;
import com.epam.jwd.dao.BetslipDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Betslip;

import java.util.List;

public class BetslipService implements BetslipBaseService {

    private static volatile BetslipService instance;
    private final BetslipBaseDao betslipDao;

    private BetslipService() {
        this.betslipDao = BetslipDao.getInstance();
    }

    public static BetslipService getInstance() {
        if (instance == null) {
            synchronized (BetslipService.class) {
                if (instance == null) {
                    instance = new BetslipService();
                }
            }
        }

        return instance;
    }

    @Override
    public Betslip save(Betslip betslip) {
        return null;
    }

    @Override
    public boolean canSave(Betslip betslip) {
        return false;
    }

    @Override
    public void update(Betslip betslip) {

    }

    @Override
    public boolean canUpdate(Betslip betslip) {
        return false;
    }

    @Override
    public List<Betslip> findAll() {
        return betslipDao.findAll();
    }

    @Override
    public Betslip findById(Long id) throws ServiceException, DaoException {
        return null;
    }

    @Override
    public void delete(Long id) throws DaoException {

    }

}