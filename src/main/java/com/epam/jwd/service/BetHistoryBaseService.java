package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BetHistory;

import java.util.List;

/**
 * A {@code BetHistoryBaseService} interface is an extending of
 * {@code BaseService} interface for bet history entity.
 *
 * @see BaseService
 * @see BetHistoryService
 */
public interface BetHistoryBaseService {

    /**
     * Looks for all history bets.
     *
     * @return a found history bets.
     */
    List<BetHistory> findAll();

    /**
     * Saves bet in the bet history by accepting bet history.
     *
     * @param betHistory a given bet history.
     * @throws DaoException     if bet wasn't saved by database causes.
     * @throws ServiceException if bet wasn't saved by application causes.
     */
    void save(BetHistory betHistory) throws DaoException, ServiceException;

}