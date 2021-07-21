package com.epam.jwd.app;

import com.epam.jwd.dao.PersonBaseDao;
import com.epam.jwd.dao.PersonDao;
import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.pool.ConnectionPoolManager;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*ConnectionPoolManager pool = ConnectionPoolManager.getInstance();

        try {
            pool.init();

            PersonBaseDao dao = PersonDao.getInstance();
            dao.delete(25L);

            pool.destroy();
        } catch (CouldNotInitializeConnectionPoolException | InterruptedException | CouldNotDestroyConnectionPoolException | DaoException e) {
            e.printStackTrace();
        }*/
    }

}