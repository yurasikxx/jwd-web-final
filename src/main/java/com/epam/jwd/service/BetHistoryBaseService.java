package com.epam.jwd.service;

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
     */
    void save(BetHistory betHistory);

}