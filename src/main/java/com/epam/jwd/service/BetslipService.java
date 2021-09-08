package com.epam.jwd.service;

import com.epam.jwd.dao.BetslipBaseDao;
import com.epam.jwd.dao.BetslipDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;

/**
 * A {@code BetslipService} class is a betslip service that is
 * an {@code BetslipBaseService} interface implementation.
 *
 * @see BaseService
 * @see BetslipBaseService
 */
public class BetslipService implements BetslipBaseService {

    private static final Logger LOGGER = LogManager.getLogger(BetslipService.class);

    private static final String BETSLIP_WAS_SAVED_MSG = "Betslip was saved";
    private static final String BETSLIP_WAS_NOT_SAVED_MSG = "Betslip wasn't saved";
    private static final String BETSLIP_WAS_UPDATED_MSG = "Betslip was updated";
    private static final String BETSLIP_WAS_NOT_UPDATED_MSG = "Betslip wasn't updated";
    private static final String BETSLIP_WAS_FOUND_MSG = "Betslip was found";
    private static final String BETSLIP_WAS_NOT_FOUND_MSG = "Betslip wasn't found";
    private static final String BETSLIPS_WERE_FOUND_MSG = "Betslips were found";
    private static final String BETSLIPS_WERE_NOT_FOUND_MSG = "Betslips weren't found";
    private static final String BETSLIP_WAS_DELETED_MSG = "Betslip was deleted";
    private static final String BETSLIPS_WAS_NOT_DELETED_MSG = "Betslips wasn't deleted";

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
    public void save(Betslip betslip) {
        try {
            betslipDao.save(betslip);
            LOGGER.info(BETSLIP_WAS_SAVED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BETSLIP_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    public void update(Betslip betslip) {
        try {
            betslipDao.update(betslip);
            LOGGER.info(BETSLIP_WAS_UPDATED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BETSLIP_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    public List<Betslip> findAll() {
        try {
            final List<Betslip> betslips = betslipDao.findAll();
            LOGGER.info(BETSLIPS_WERE_FOUND_MSG);

            return betslips;
        } catch (DaoException e) {
            LOGGER.error(BETSLIPS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public Betslip findById(Long id) {
        Betslip betslip = null;

        try {
            final Optional<Betslip> optional = betslipDao.findById(id);

            if (optional.isPresent()) {
                betslip = optional.get();
            }

            LOGGER.info(BETSLIP_WAS_FOUND_MSG);
        } catch (DaoException e) {
            LOGGER.error(BETSLIP_WAS_NOT_FOUND_MSG);
        }

        return betslip;
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            betslipDao.delete(id);
            LOGGER.info(BETSLIP_WAS_DELETED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BETSLIPS_WAS_NOT_DELETED_MSG);
            throw new ServiceException(BETSLIPS_WAS_NOT_DELETED_MSG);
        }
    }

    @Override
    public boolean canBeDeleted(Long id) {
        final List<Betslip> betslips = this.findAll();
        final Betslip betslip = this.findById(id);

        return betslips.contains(betslip) && id > MIN_INDEX_VALUE;
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
    public List<Betslip> findByCompetitionId(Long id) {
        try {
            final List<Betslip> betslips = betslipDao.findByCompetitionId(id);
            LOGGER.info(BETSLIPS_WERE_FOUND_MSG);

            return betslips;
        } catch (DaoException e) {
            LOGGER.error(BETSLIPS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Betslip> findByBetType(BetslipType betslipType) {
        try {
            final List<Betslip> betslips = betslipDao.findByBetType(betslipType);
            LOGGER.info(BETSLIPS_WERE_FOUND_MSG);

            return betslips;
        } catch (DaoException e) {
            LOGGER.error(BETSLIPS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

}