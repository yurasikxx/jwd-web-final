package com.epam.jwd.service;

import com.epam.jwd.dao.BetBaseDao;
import com.epam.jwd.dao.BetDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Bet;

import java.util.ArrayList;
import java.util.List;

public class BetService implements BetBaseService {

    private static final String BET_WAS_NOT_FIND_BY_GIVEN_ID_MSG = "Bet wasn't find by given ID";
    private static volatile BetService instance;
    private final BetBaseDao betDao;

    private BetService() {
        this.betDao = BetDao.getInstance();
    }

    public static BetService getInstance() {
        if (instance == null) {
            synchronized (BetService.class) {
                if (instance == null) {
                    instance = new BetService();
                }
            }
        }

        return instance;
    }

    @Override
    public List<Bet> findAll() {
        return betDao.findAll();
    }

    @Override
    public Bet findById(Long id) throws ServiceException, DaoException {
        return betDao.findById(id).
                orElseThrow(() -> new ServiceException(String.format(BET_WAS_NOT_FIND_BY_GIVEN_ID_MSG, id)));
    }

    @Override
    public void delete(Long id) throws DaoException {
        betDao.delete(id);
    }

    @Override
    public boolean canBeDeleted(Long id) throws ServiceException, DaoException {
        return this.findAll().contains(this.findById(id)) && id > 0;
    }

    @Override
    public void save(Bet bet) throws DaoException {
        betDao.save(bet);
    }

    @Override
    public boolean canSave(Bet bet) {
        final List<Bet> bets = this.findAll();
        final List<Bet> theseWithoutBetTotalBets = new ArrayList<>();
        final Bet withoutBetTotalBet = new Bet(bet.getBetslip(), bet.getPerson());

        for (Bet iteratedBetslip : bets) {
            theseWithoutBetTotalBets.add(new Bet(iteratedBetslip.getBetslip(), iteratedBetslip.getPerson()));
        }

        return !theseWithoutBetTotalBets.contains(withoutBetTotalBet);
    }

    @Override
    public void update(Bet bet) throws DaoException {
        betDao.update(bet);
    }

}