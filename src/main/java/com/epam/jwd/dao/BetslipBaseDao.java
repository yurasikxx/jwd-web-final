package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Betslip;

import java.util.List;

public interface BetslipBaseDao extends BaseDao<Betslip> {

    List<Betslip> findByCoefficient(double coefficient) throws DaoException;

}
