package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;

import java.util.List;

public interface BetBaseService extends BaseService<Bet> {

    boolean canSave(Bet bet);

    List<Bet> findByCompetitionId(Long id) throws DaoException;

}