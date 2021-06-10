package com.epam.jwd.dao;

import com.epam.jwd.model.Bet;

import java.util.Optional;

public interface BetBaseDao extends BaseDao<Bet> {

    Optional<Bet> findBetByName(String name);

}