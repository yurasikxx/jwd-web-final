package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;

import java.util.List;

public interface CompetitionBaseDao extends BaseDao<Competition> {

    List<Competition> findBySportName(Sport sport) throws DaoException;

    Team findTeamById(Long id) throws DaoException;

}