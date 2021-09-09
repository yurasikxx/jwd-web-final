package com.epam.jwd.service;

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
     */
    Team findTeamById(Long id);

    /**
     * Looks for competitions by accepting sport and returns them.
     *
     * @param sport a given sport.
     * @return a found competitions.
     */
    List<Competition> findBySportName(Sport sport);

    /**
     * Looks for all teams.
     *
     * @return a found competitions.
     */
    List<Team> findAllTeams();

    /**
     * Looks for teams by accepting sport and returns them.
     *
     * @param sport a given sport.
     * @return a found teams.
     */
    List<Team> findTeamsBySportName(Sport sport);

}