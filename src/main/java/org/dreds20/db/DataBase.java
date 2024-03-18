package org.dreds20.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase implements AutoCloseable {
    private Connection connection = null;

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/server", "server", "tempPassword");
        }
        return connection;
    }

    @Override
    public void close() throws Exception {

    }
}
