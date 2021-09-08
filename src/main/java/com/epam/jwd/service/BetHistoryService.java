package com.epam.jwd.service;

import com.epam.jwd.dao.BetHistoryDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BetHistory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

/**
 * A {@code BetHistoryService} class is a bet history service that is
 * an {@code BetHistoryBaseService} interface implementation.
 *
 * @see BaseService
 * @see BetHistoryBaseService
 */
public class BetHistoryService implements BetHistoryBaseService {

    private static final Logger LOGGER = LogManager.getLogger(BetHistoryService.class);

    private static final String BET_WAS_SAVED_MSG = "Bet was saved";
    private static final String BET_WAS_NOT_SAVED_MSG = "Bet wasn't saved";
    private static final String BETS_WERE_FOUND_MSG = "Bets were found";
    private static final String BETS_WERE_NOT_FOUND_MSG = "Bets weren't found";

    private static volatile BetHistoryService instance;

    private final BetHistoryDao betHistoryDao;

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
    public void save(BetHistory betHistory) {
        try {
            betHistoryDao.save(betHistory);
            LOGGER.info(BET_WAS_SAVED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BET_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    public List<BetHistory> findAll() {
        try {
            final List<BetHistory> bets = betHistoryDao.findAll();
            LOGGER.info(BETS_WERE_FOUND_MSG);

            return bets;
        } catch (DaoException e) {
            LOGGER.error(BETS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

}