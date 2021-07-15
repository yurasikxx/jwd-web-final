package com.epam.jwd.listener;

import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.pool.ConnectionPoolManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPoolManager.getInstance().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ConnectionPoolManager.getInstance().destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
