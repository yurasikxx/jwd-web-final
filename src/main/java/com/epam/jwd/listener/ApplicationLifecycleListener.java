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

    private static final String CONNECTION_POOL_WAS_NOT_INITIALIZED_MSG = "Connection pool wasn't initialized";
    private static final String CONNECTION_POOL_WAS_INITIALIZED_MSG = "Connection pool was initialized";
    private static final String CONNECTION_POOL_WAS_DESTROYED_MSG = "Connection pool was destroyed";
    private static final String CONNECTION_POOL_WAS_NOT_DESTROYED_MSG = "Connection pool wasn't destroyed";

    private final ConnectionPoolManager pool;

    public ApplicationLifecycleListener() {
        pool = ConnectionPoolManager.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            pool.init();
            LOGGER.info(CONNECTION_POOL_WAS_INITIALIZED_MSG);
        } catch (CouldNotInitializeConnectionPoolException e) {
            LOGGER.error(CONNECTION_POOL_WAS_NOT_INITIALIZED_MSG);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            pool.destroy();
            LOGGER.info(CONNECTION_POOL_WAS_DESTROYED_MSG);
        } catch (CouldNotDestroyConnectionPoolException | InterruptedException e) {
            LOGGER.info(CONNECTION_POOL_WAS_NOT_DESTROYED_MSG);
        }
    }

}