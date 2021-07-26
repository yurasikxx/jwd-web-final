package com.epam.jwd.service;

import com.epam.jwd.model.Bet;

public interface BetBaseService extends BaseService<Bet> {

    boolean canSave(Bet bet);

}
