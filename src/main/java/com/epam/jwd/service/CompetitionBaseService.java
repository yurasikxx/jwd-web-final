package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;

import java.util.List;

/**
 * A {@code CompetitionBaseService} interface is an extending of
 * {@code BaseService} interface for competition entity.
 *
 * @see BaseService
 * @see CompetitionService
 */
public interface CompetitionBaseService extends BaseService<Competition> {

    /**
     * Indicates if given competition can be saved.
     *
     * @param competition a given competition.
     * @return {@code true} if competition can be saved; {@code false} otherwise.
     */
    boolean canSave(Competition competition);

    /**
     * Looks for team by accepting team ID and returns it.
     *
     * @param id a given team ID.
     * @return a found team.
     * @throws DaoException     if team wasn't found by database causes.
     * @throws ServiceException if team wasn't found by application causes.
     */
    Team findTeamById(Long id) throws DaoException, ServiceException;

    /**
     * Looks for competitions by accepting sport and returns them.
     *
     * @param sport a given sport.
     * @return a found competitions.
     * @throws DaoException if competitions weren't found.
     */
    List<Competition> findBySportName(Sport sport) throws DaoException;

    /**
     * Looks for all teams.
     *
     * @return a found competitions.
     */
    List<Team> findAllTeams();

}