package com.epam.jwd.service;

import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;

import java.util.List;

/**
 * A {@code BetslipBaseService} interface is an extending of
 * {@code BaseService} interface for betslip entity.
 *
 * @see BaseService
 * @see BetslipService
 */
public interface BetslipBaseService extends BaseService<Betslip> {

    /**
     * Indicates if given betslip can be saved.
     *
     * @param betslip a given betslip.
     * @return {@code true} if betslip can be saved; {@code false} otherwise.
     */
    boolean canSave(Betslip betslip);

    /**
     * Looks for betslips by accepting competition ID and returns them.
     *
     * @param id a given competition ID.
     * @return a found betslips.
     */
    List<Betslip> findByCompetitionId(Long id);

    /**
     * Looks for betslips by accepting betslip type and returns them.
     *
     * @param betslipType a given betslip type.
     * @return a found betslips.
     */
    List<Betslip> findByBetType(BetslipType betslipType);

}