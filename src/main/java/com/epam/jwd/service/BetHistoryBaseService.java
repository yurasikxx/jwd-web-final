package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BetHistory;

import java.util.List;

public interface BetHistoryBaseService {

    List<BetHistory> findAll();

    void save(BetHistory betHistory) throws DaoException, ServiceException;

}