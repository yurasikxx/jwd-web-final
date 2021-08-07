package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;

import java.util.List;

/**
 * A {@code CompetitionBaseDao} interface is an extending of
 * {@code BaseDao} interface for competition entity.
 *
 * @see BaseDao
 * @see CommonDao
 * @see CompetitionDao
 */
public interface CompetitionBaseDao extends BaseDao<Competition> {

    /**
     * Looks for competitions by given sport and returns them.
     *
     * @param sport a given competition sport.
     * @return a found competitions.
     * @throws DaoException if competitions weren't found by given sport.
     */
    List<Competition> findBySportName(Sport sport) throws DaoException;

}