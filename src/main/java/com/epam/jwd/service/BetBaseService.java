package com.epam.jwd.service;

import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetType;

import java.util.List;

/**
 * A {@code BetBaseService} interface is an extending of
 * {@code BaseService} interface for bet entity.
 *
 * @see BaseService
 * @see BetService
 */
public interface BetBaseService extends BaseService<Bet> {

    /**
     * Indicate if given bet can be saved.
     *
     * @param bet a given bet.
     * @return {@code true} if bet can be saved; {@code false} otherwise.
     */
    boolean canSave(Bet bet);

    /**
     * Looks for bets by accepting competition ID and returns them.
     *
     * @param id a given competition ID.
     * @return a found bets.
     */
    List<Bet> findByCompetitionId(Long id);

    /**
     * Looks for bets by accepting bet type and returns them.
     *
     * @param betType a given bet type.
     * @return a found bets.
     */
    List<Bet> findByBetType(BetType betType);

}