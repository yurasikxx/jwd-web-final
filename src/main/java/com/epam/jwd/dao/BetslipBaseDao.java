package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;

import java.util.List;

public interface BetslipBaseDao extends BaseDao<Betslip> {

    List<Betslip> findByCoefficient(double coefficient) throws DaoException;

    List<Betslip> findByCompetitionId(Long id) throws DaoException;

}
