package org.dreds20.db;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DataBaseConfigTest {
    private static final String URL = "testUrl";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testUrl";

    @Test
    void cannotCreateConfigWithoutURL() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> DataBaseConfig.builder().username(USERNAME).password(PASSWORD).build());
    }

    @Test
    void cannotCreateConfigWithoutUsername() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> DataBaseConfig.builder().url(URL).password(PASSWORD).build());
    }

    @Test
    void cannotCreateConfigWithoutPassword() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> DataBaseConfig.builder().url(URL).username(USERNAME).build());
    }

    @Test
    void cannotCreateConfigWithNullURL() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DataBaseConfig.builder().url(null).username(USERNAME).password(PASSWORD).build());
    }

    @Test
    void cannotCreateConfigWithNullUsername() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DataBaseConfig.builder().url(URL).username(null).password(PASSWORD).build());
    }

    @Test
    void cannotCreateConfigWithNullPassword() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DataBaseConfig.builder().url(URL).username(USERNAME).password(null).build());
    }

    @Test
    void urlCanBeSet() {
        DataBaseConfig config = DataBaseConfig.builder()
                .url(URL)
                .username(USERNAME)
                .password(PASSWORD).build();
        assertThat(config.url()).isEqualTo(URL);
    }

    @Test
    void usernameCanBeSet() {
        DataBaseConfig config = DataBaseConfig.builder()
                .url(URL)
                .username(USERNAME)
                .password(PASSWORD).build();
        assertThat(config.username()).isEqualTo(USERNAME);
    }

    @Test
    void passwordCanBeSet() {
        DataBaseConfig config = DataBaseConfig.builder()
                .url(URL)
                .username(USERNAME)
                .password(PASSWORD).build();
        assertThat(config.password()).isEqualTo(PASSWORD);
    }

    @Test
    void builderWithUnary() {
        DataBaseConfig config = DataBaseConfig.builder(builder -> builder
                .url(URL)
                .username(USERNAME)
                .password(PASSWORD)).build();
        assertThat(config.url()).isEqualTo(URL);
        assertThat(config.username()).isEqualTo(USERNAME);
        assertThat(config.password()).isEqualTo(PASSWORD);
    }

    @Test
    void createMethod() {
        DataBaseConfig config = DataBaseConfig.create(builder -> builder
                .url(URL)
                .username(USERNAME)
                .password(PASSWORD));
        assertThat(config.url()).isEqualTo(URL);
        assertThat(config.username()).isEqualTo(USERNAME);
        assertThat(config.password()).isEqualTo(PASSWORD);
    }
}
