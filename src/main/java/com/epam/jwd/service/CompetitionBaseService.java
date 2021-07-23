package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;

import java.util.List;

public interface CompetitionBaseService extends BaseService<Competition> {

    boolean canSave(Competition competition);

    boolean canUpdate(Competition competition);

    Team findTeamById(Long id) throws DaoException;

    List<Competition> findBySportName(Sport sport) throws DaoException;

}
