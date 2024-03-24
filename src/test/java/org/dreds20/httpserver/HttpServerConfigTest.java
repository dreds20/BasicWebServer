package org.dreds20.httpserver;

import org.dreds20.httpserver.pages.FileContentLoader;
import org.dreds20.httpserver.pages.PageManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class HttpServerConfigTest {
    @Mock
    PageManager pageManagerMock;

    @Mock
    FileContentLoader contentLoaderMock;

    @Test
    void nullPointerWhenNullConnectionManagerProvided() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> HttpServerConfig.builder()
                .connectionManager((SimpleConnectionManager) null)
                .executorService(Executors.newFixedThreadPool(1)).build());
    }

    @Test
    void cannotCreateConfigWithoutConnectionManager() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> HttpServerConfig.builder()
                .executorService(Executors.newFixedThreadPool(1)).build());
    }

    @Test
    void nullPointerWhenNullExecutorServiceProvided() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> HttpServerConfig.builder()
                .executorService((ExecutorService) null)
                .connectionManager(new SimpleConnectionManager(pageManagerMock, contentLoaderMock)).build());
    }

    @Test
    void defaultExecutorService() {
        HttpServerConfig config = HttpServerConfig.builder()
                .connectionManager(new SimpleConnectionManager(pageManagerMock, contentLoaderMock)).build();
        assertThat(config.executorService()).isInstanceOf(ExecutorService.class);
    }

    @Test
    void defaultPortIs80() {
        HttpServerConfig config = HttpServerConfig.create(builder -> builder
                .connectionManager((new SimpleConnectionManager(pageManagerMock, contentLoaderMock)))
                .executorService(Executors.newFixedThreadPool(1)));
        assertThat(config.port()).isEqualTo(80);
    }
}
