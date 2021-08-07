package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;

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
     * @throws DaoException if betslips weren't found.
     */
    List<Betslip> findByCompetitionId(Long id) throws DaoException;

    /**
     * Looks for betslips by accepting bet type and returns them.
     *
     * @param betType a given bet type.
     * @return a found betslips.
     * @throws DaoException if betslips weren't found.
     */
    List<Betslip> findByBetType(BetType betType) throws DaoException;

}