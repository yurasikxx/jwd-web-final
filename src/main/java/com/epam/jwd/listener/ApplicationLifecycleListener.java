package com.epam.jwd.listener;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationLifecycleListener.class);

    private final ConnectionPoolManager pool;

    public ApplicationLifecycleListener() {
        pool = ConnectionPoolManager.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            pool.init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            pool.destroy();
        } catch (CouldNotDestroyConnectionPoolException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}