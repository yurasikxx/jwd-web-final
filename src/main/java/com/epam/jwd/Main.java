package com.epam.jwd;

import com.epam.jwd.dao.BetslipBaseDao;
import com.epam.jwd.dao.BetslipDao;
import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;
import com.epam.jwd.pool.ConnectionPoolManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
/*        ConnectionPoolManager pool = ConnectionPoolManager.getInstance();

        try {
            pool.init();


            BetslipBaseDao dao = BetslipDao.getInstance();
            System.out.println(dao.findById(1L).get());


            pool.destroy();
        } catch (CouldNotInitializeConnectionPoolException | InterruptedException | CouldNotDestroyConnectionPoolException | DaoException e) {
            e.printStackTrace();
        }*/
    }

}