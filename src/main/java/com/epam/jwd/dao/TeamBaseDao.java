package com.epam.jwd.dao;

import com.epam.jwd.model.Team;

import java.util.List;

public interface TeamBaseDao extends BaseDao<Team> {

    List<Team> findByCountry(String country);

    List<Team> findByRate(int rate);

}