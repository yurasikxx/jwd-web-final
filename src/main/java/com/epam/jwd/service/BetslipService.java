package com.epam.jwd.service;

import com.epam.jwd.dao.BetslipBaseDao;
import com.epam.jwd.dao.BetslipDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BetslipType;
import com.epam.jwd.model.Betslip;

import java.util.ArrayList;
import java.util.List;

import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;

/**
 * A {@code BetslipService} class is a betslip service that is
 * an {@code BetslipBaseService} interface implementation.
 *
 * @see BaseService
 * @see BetslipBaseService
 */
public class BetslipService implements BetslipBaseService {

    private static final String BETSLIP_WAS_NOT_FIND_BY_GIVEN_ID_MSG = "Betslip wasn't find by given id";

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
    public void save(Betslip betslip) throws DaoException {
        betslipDao.save(betslip);
    }

    @Override
    public boolean canSave(Betslip betslip) {
        final List<Betslip> betslips = this.findAll();
        final List<Betslip> theseWithoutCoefficientBetslips = new ArrayList<>();
        final Betslip withoutCoefficientBetslip = new Betslip(betslip.getCompetition(), betslip.getBetslipType());

        for (Betslip iteratedBetslip : betslips) {
            theseWithoutCoefficientBetslips.add(new Betslip(iteratedBetslip.getCompetition(), iteratedBetslip.getBetslipType()));
        }

        return !theseWithoutCoefficientBetslips.contains(withoutCoefficientBetslip);
    }

    @Override
    public void update(Betslip betslip) throws DaoException {
        betslipDao.update(betslip);
    }

    @Override
    public List<Betslip> findAll() {
        return betslipDao.findAll();
    }

    @Override
    public Betslip findById(Long id) throws ServiceException, DaoException {
        return betslipDao.findById(id).
                orElseThrow(() -> new ServiceException(String.format(BETSLIP_WAS_NOT_FIND_BY_GIVEN_ID_MSG, id)));
    }

    @Override
    public List<Betslip> findByCompetitionId(Long id) throws DaoException {
        return betslipDao.findByCompetitionId(id);
    }

    @Override
    public List<Betslip> findByBetType(BetslipType betslipType) throws DaoException {
        return betslipDao.findByBetType(betslipType);
    }

    @Override
    public void delete(Long id) throws DaoException {
        betslipDao.delete(id);
    }

    @Override
    public boolean canBeDeleted(Long id) throws ServiceException, DaoException {
        return this.findAll().contains(this.findById(id)) && id > MIN_INDEX_VALUE;
    }

}