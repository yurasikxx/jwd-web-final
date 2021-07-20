package com.epam.jwd.service;

import com.epam.jwd.model.Betslip;

public interface BetslipBaseService extends BaseService<Betslip> {

    void save(Betslip betslip);

    boolean canSave(Betslip betslip);

    void update(Betslip betslip);

    boolean canUpdate(Betslip betslip);

}
