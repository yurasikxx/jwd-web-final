package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;

import java.util.List;

/**
 * A {@code TeamBaseDao} interface is an extending of
 * {@code BaseDao} interface for team entity.
 *
 * @see BaseDao
 * @see CommonDao
 * @see TeamDao
 */
public interface TeamBaseDao extends BaseDao<Team> {

    /**
     * Looks for teams by given sport and returns them.
     *
     * @param sport a given sport.
     * @return a found teams.
     * @throws DaoException if teams weren't found by given sport.
     */
    List<Team> findBySportName(Sport sport) throws DaoException;

}