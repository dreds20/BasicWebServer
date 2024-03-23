package org.dreds20.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DataBase class is responsible for establishing a connection to the database specified in provided
 * configuration object.
 *
 * Requires a non-null configuration object.
 */
public class DataBase {
    private static final Logger log = LogManager.getLogger(DataBase.class.getName());
    private Connection connection = null;
    private final DataBaseConfig config;

    public DataBase(DataBaseConfig config) {
        if (config == null) {
            throw new NoDatabaseConfigException("No config provided when initialising DataBase object");
        }
        this.config = config;
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            log.info("Making connection to db..");
            connection = DriverManager.getConnection(config.url(), config.username(), config.password());
            log.info("Connection to db established..");
        }
        return connection;
    }
}
