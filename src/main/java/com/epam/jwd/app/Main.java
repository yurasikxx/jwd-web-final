package com.epam.jwd.app;

import com.epam.jwd.dao.BetDao;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("start");

        try {
            ConnectionPoolManager.getInstance().init();
            BetDao betDao = new BetDao();
            betDao.findAll().forEach(System.out::println);
            System.out.println(betDao.findById(2L));
            System.out.println(betDao.findBetByName("Milan - PSG. Match winner: PSG"));
            ConnectionPoolManager.getInstance().destroy();
        } catch (CouldNotInitializeConnectionPoolException | DaoException | InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("end");
    }

}