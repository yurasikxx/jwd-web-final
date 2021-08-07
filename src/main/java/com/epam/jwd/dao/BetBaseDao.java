package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;

import java.util.List;

/**
 * A {@code BetBaseDao} interface is an extending of
 * {@code BaseDao} interface for bet entity.
 *
 * @see BaseDao
 * @see CommonDao
 * @see BetDao
 */
public interface BetBaseDao extends BaseDao<Bet> {

    /**
     * Looks for bets by given competition id and returns found bets.
     *
     * @param id a given competition id.
     * @return a found bets.
     * @throws DaoException if bets weren't found by given competition id.
     */
    List<Bet> findByCompetitionId(Long id) throws DaoException;

}