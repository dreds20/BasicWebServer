package org.dreds20.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase implements AutoCloseable {
    private static final Logger log = LogManager.getLogger(DataBase.class.getName());
    private Connection connection = null;

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            log.info("Making connection to db..");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/server", "server", "tempPassword");
            log.info("Connection to db established..");
        }
        return connection;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
