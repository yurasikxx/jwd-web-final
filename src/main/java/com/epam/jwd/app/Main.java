package com.epam.jwd.app;

import com.epam.jwd.dao.BetBaseDao;
import com.epam.jwd.dao.BetDao;
import com.epam.jwd.dao.BetslipBaseDao;
import com.epam.jwd.dao.BetslipDao;
import com.epam.jwd.dao.CompetitionBaseDao;
import com.epam.jwd.dao.CompetitionDao;
import com.epam.jwd.dao.PersonBaseDao;
import com.epam.jwd.dao.PersonDao;
import com.epam.jwd.dao.TeamDao;
import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.BetType;
import com.epam.jwd.model.Betslip;
import com.epam.jwd.model.Competition;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Sport;
import com.epam.jwd.model.Team;
import com.epam.jwd.pool.ConnectionPoolManager;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("".length());
        ConnectionPoolManager pool = ConnectionPoolManager.getInstance();

        try {
            pool.init();

/*            PersonBaseDao personDao = PersonDao.getInstance();
            final Person person = new Person("bukayo_saka", "bukayo");
            personDao.save(person);*/

/*
            TeamDao teamDao = TeamDao.getInstance();
            Team home = null;
            Team away = null;


            if (teamDao.findById(1L).isPresent()) {
                home = teamDao.findById(1L).get();
            }
            if (teamDao.findById(5L).isPresent()) {
                away = teamDao.findById(5L).get();
            }

            final Competition competition = new Competition(Sport.resolveSportById(1L), home, away);
            competitionDao.save(competition);
*/
            CompetitionBaseDao competitionDao = CompetitionDao.getInstance();

            BetslipBaseDao betslipDao = BetslipDao.getInstance();

            Competition competition1 = null;

            if (competitionDao.findById(1L).isPresent()) {
                competition1 = competitionDao.findById(1L).get();
            }

            final Betslip betslip = new Betslip(competition1, BetType.AWAY_TEAM_WILL_NOT_LOSE, 1.65);
            betslipDao.save(betslip);

/*
            BetBaseDao betDao = BetDao.getInstance();

            Betslip betslip1 = null;

            if (betslipDao.findById(2L).isPresent()) {
                betslip1 = betslipDao.findById(2L).get();
            }

            Person person1 = null;

            if (personDao.findById(2L).isPresent()) {
                person1 = personDao.findById(2L).get();
            }

            final Bet bet = new Bet(betslip1, 500, person1);
            betDao.save(bet);
*/

            pool.destroy();
        } catch (CouldNotInitializeConnectionPoolException | InterruptedException | CouldNotDestroyConnectionPoolException | DaoException e) {
            e.printStackTrace();
        }
    }

}