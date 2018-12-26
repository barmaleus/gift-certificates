package by.rekuts.giftcertificates.repository;

import org.springframework.jdbc.datasource.AbstractDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

public class CustomDataSource extends AbstractDataSource implements Closeable {
    private volatile CustomConnectionPool pool = CustomConnectionPool.getInstance();
    private volatile Connection connection;

    @Override
    public Connection getConnection() throws SQLException {
        connection = pool.getConnection();
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException("getConnection with username and password in signature");
    }

    @Override
    public void close() {
        pool.returnConnectionToThePool(connection);
        connection = null;
    }
}
