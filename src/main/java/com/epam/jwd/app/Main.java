package com.epam.jwd.app;

import com.epam.jwd.dao.BetDao;
import com.epam.jwd.dao.BetslipDao;
import com.epam.jwd.dao.CompetitionDao;
import com.epam.jwd.dao.PersonDao;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Role;
import com.epam.jwd.model.Sport;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("start");

        try {
            ConnectionPoolManager.getInstance().init();

            PersonDao personDao = new PersonDao();
            System.out.println("person dao created");
            System.out.println("person find all");
            personDao.findAll().forEach(System.out::println);
            System.out.println("person find by id");
            System.out.println(personDao.findById(1L));
            System.out.println("person custom find");
            personDao.findByRole(Role.USER).forEach(System.out::println);

            System.out.println("-------------------------------------------------------------------------------------");

            CompetitionDao competitionDao = new CompetitionDao();
            System.out.println("competition dao created");
            System.out.println("competition find all");
            competitionDao.findAll().forEach(System.out::println);
            System.out.println("competition find by id");
            System.out.println(competitionDao.findById(2L));
            System.out.println("competition custom find");
            competitionDao.findBySportName(Sport.FOOTBALL).forEach(System.out::println);

            System.out.println("-------------------------------------------------------------------------------------");

            BetslipDao betslipDao = new BetslipDao();
            System.out.println("betslip dao created");
            System.out.println("betslip find all");
            betslipDao.findAll().forEach(System.out::println);
            System.out.println("betslip find by id");
            System.out.println(betslipDao.findById(3L));
            System.out.println("betslip custom find");
            betslipDao.findByCoefficient(3.06).forEach(System.out::println);

            System.out.println("-------------------------------------------------------------------------------------");

            BetDao betDao = new BetDao();
            System.out.println("bet dao created");
            System.out.println("bet find all");
            betDao.findAll().forEach(System.out::println);
            System.out.println("bet find by id");
            System.out.println(betDao.findById(4L));
            System.out.println("bet custom find");
            betDao.findByTotal(840).forEach(System.out::println);

            ConnectionPoolManager.getInstance().destroy();
        } catch (CouldNotInitializeConnectionPoolException | DaoException | InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("end");
    }

}