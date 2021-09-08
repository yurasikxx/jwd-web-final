package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.BetslipType;

import java.util.List;

/**
 * A {@code BetslipBaseDao} interface is an extending of
 * {@code BaseDao} interface for betslip entity.
 *
 * @see BaseDao
 * @see CommonDao
 * @see BetslipDao
 */
public interface BetslipBaseDao extends BaseDao<Betslip> {

    /**
     * Looks for betslips by given betslip type and returns found betslips.
     *
     * @param betslipType a given betslip type.
     * @return a found betslips.
     * @throws DaoException if betslips weren't found by given bet type.
     */
    List<Betslip> findByBetType(BetslipType betslipType) throws DaoException;

    /**
     * Looks for betslips by given id and returns found betslips.
     *
     * @param id a given id.
     * @return a found betslips.
     * @throws DaoException if betslips weren't found by given id.
     */
    List<Betslip> findByCompetitionId(Long id) throws DaoException;

}