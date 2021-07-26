package com.epam.jwd.service;

import com.epam.jwd.model.Betslip;

public interface BetslipBaseService extends BaseService<Betslip> {

    boolean canSave(Betslip betslip);

}
