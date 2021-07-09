package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;

import java.util.List;

public interface BetBaseDao extends BaseDao<Bet> {

    List<Bet> findByTotal(Integer betTotal) throws DaoException;

}