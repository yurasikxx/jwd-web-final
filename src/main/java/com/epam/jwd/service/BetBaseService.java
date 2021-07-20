package com.epam.jwd.service;

import com.epam.jwd.model.Bet;

public interface BetBaseService extends BaseService<Bet> {

    void save(Bet bet);

    boolean canSave(Bet bet);

    void update(Bet bet);

    boolean canUpdate(Bet bet);

}
