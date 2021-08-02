package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;

import java.util.List;

public interface BetslipBaseService extends BaseService<Betslip> {

    boolean canSave(Betslip betslip);

    List<Betslip> findByCompetitionId(Long id) throws DaoException;

    List<Betslip> findByBetType(BetType betType) throws DaoException;

}