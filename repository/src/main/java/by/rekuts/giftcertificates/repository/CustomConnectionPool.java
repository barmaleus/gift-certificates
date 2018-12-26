package by.rekuts.giftcertificates.repository;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.concurrent.locks.ReentrantLock;

public class CustomConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(CustomConnectionPool.class.getName());
    private ArrayDeque<Connection> pool = new ArrayDeque<>();
    private static final int MAX_CONNECTIONS = 6;
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/gcerts";
    private final ReentrantLock getConnectionLock = new ReentrantLock();
    private final ReentrantLock returnConnectionLock = new ReentrantLock();

    private CustomConnectionPool() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARN, "Can't load postgres driver. ", e);
        }
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            pool.add(createNewConnection());
        }
    }

    private static CustomConnectionPool instance = null;
    public static CustomConnectionPool getInstance() {
        if (instance == null) {
            instance = new CustomConnectionPool();
        }
        return instance;
    }

    private Connection createNewConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "SQL expeption while creating Connection. " , e);
        }
        return connection;
    }

    public Connection getConnection() throws SQLException{
        Connection connection = null;
        getConnectionLock.lock();
        try {
            if (pool.size() > 0) {
                connection = pool.poll(); //retrieves connection and removes it from pool begining. If query is empty, returns null
            }
        } finally {
            getConnectionLock.unlock();
        }
        return connection;
    }

    public void returnConnectionToThePool(Connection connection) {
        returnConnectionLock.lock();
        try {
            pool.add(connection);
        } finally {
            returnConnectionLock.unlock();
        }
    }

    public void closeConnectionsInPool() {
        Connection connection;
        try {
            while(pool.size() > 0) {
                connection = pool.poll();
                connection.close();
            }
            LOGGER.log(Level.INFO, "Connections in pool have been closed.");
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Connections in pool haven't been closed. " , e);
        }

    }
}