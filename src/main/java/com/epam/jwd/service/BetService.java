package com.epam.jwd.service;

import com.epam.jwd.dao.BetBaseDao;
import com.epam.jwd.dao.BetDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;

/**
 * A {@code BetService} class is a bet service that is
 * an {@code BetBaseService} interface implementation.
 *
 * @see BaseService
 * @see BetBaseService
 */
public class BetService implements BetBaseService {

    private static final Logger LOGGER = LogManager.getLogger(BetService.class);

    private static final String BET_WAS_SAVED_MSG = "Bet was saved";
    private static final String BET_WAS_NOT_SAVED_MSG = "Bet wasn't saved";
    private static final String BET_WAS_UPDATED_MSG = "Bet was updated";
    private static final String BET_WAS_NOT_UPDATED_MSG = "Bet wasn't updated";
    private static final String BETS_WERE_FOUND_MSG = "Bets were found";
    private static final String BETS_WERE_NOT_FOUND_MSG = "Bets weren't found";
    private static final String BET_WAS_FOUND_MSG = "Bet was found";
    private static final String BET_WAS_NOT_FOUND_MSG = "Bet wasn't found";
    private static final String BET_WAS_DELETED_MSG = "Bet was deleted";
    private static final String BET_WAS_NOT_DELETED_MSG = "Bet wasn't deleted";

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
    public void save(Bet bet) {
        try {
            betDao.save(bet);
            LOGGER.info(BET_WAS_SAVED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BET_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    public void update(Bet bet) {
        try {
            betDao.update(bet);
            LOGGER.info(BET_WAS_UPDATED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BET_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    public List<Bet> findAll() {
        try {
            final List<Bet> bets = betDao.findAll();
            LOGGER.info(BETS_WERE_FOUND_MSG);

            return bets;
        } catch (DaoException e) {
            LOGGER.error(BETS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public Bet findById(Long id) {
        Bet bet = null;

        try {
            final Optional<Bet> optional = betDao.findById(id);

            if (optional.isPresent()) {
                bet = optional.get();
            }

            LOGGER.info(BET_WAS_FOUND_MSG);
        } catch (DaoException e) {
            LOGGER.error(BET_WAS_NOT_FOUND_MSG);
        }

        return bet;
    }

    @Override
    public void delete(Long id) {
        try {
            betDao.delete(id);
            LOGGER.info(BET_WAS_DELETED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BET_WAS_NOT_DELETED_MSG);
        }
    }

    @Override
    public boolean canBeDeleted(Long id) {
        final List<Bet> bets = this.findAll();
        final Bet bet = this.findById(id);

        return bets.contains(bet) && id > MIN_INDEX_VALUE;
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
    public List<Bet> findByCompetitionId(Long id) {
        try {
            final List<Bet> bets = betDao.findByCompetitionId(id);
            LOGGER.info(BETS_WERE_FOUND_MSG);

            return bets;
        } catch (DaoException e) {
            LOGGER.error(BETS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Bet> findByBetType(BetType betType) {
        try {
            final List<Bet> bets = betDao.findByBetType(betType);
            LOGGER.info(BETS_WERE_FOUND_MSG);

            return bets;
        } catch (DaoException e) {
            LOGGER.error(BETS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

}