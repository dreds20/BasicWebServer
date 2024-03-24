package org.dreds20.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DataBaseTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:alpine3.19"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    DataBase setup(String url, String username, String password) {
        return new DataBase(DataBaseConfig.create(builder -> builder
                .url(url)
                .username(username)
                .password(password)));
    }

    DataBase setupValid() {
        return setup(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }

    @Test
    void connectionEstablishedWithValidConfig() throws SQLException {
        DataBase dataBase = setupValid();
        assertThat(dataBase.connect()).isNotNull();
    }

    @Test
    void connectionFailureWithEmptyUrl() {
        DataBase dataBase = setup("", postgres.getUsername(), postgres.getPassword());
        assertThatExceptionOfType(SQLException.class).isThrownBy(dataBase::connect);
    }

    @Test
    void connectionFailureWithEmptyUsername() {
        DataBase dataBase = setup(postgres.getJdbcUrl(), "", postgres.getPassword());
        assertThatExceptionOfType(SQLException.class).isThrownBy(dataBase::connect);
    }

    @Test
    void connectionFailureWithEmptyPassword() {
        DataBase dataBase = setup(postgres.getJdbcUrl(), postgres.getUsername(), "");
        assertThatExceptionOfType(SQLException.class).isThrownBy(dataBase::connect);
    }

    @Test
    void DatabaseInitialisationFailureWhenNullConfigProvided() {
        assertThatExceptionOfType(NoDatabaseConfigException.class).isThrownBy(() -> new DataBase(null));
    }


}
