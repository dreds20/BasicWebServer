package org.dreds20.httpserver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpServerTest {
    @Mock
    HttpServerConfig configMock;

    @Mock
    ExecutorService executorServiceMock;

    @Mock
    ConnectionManager connectionManagerMock;


    final int PORT = 49152;

    @Test
    void nullPointerExceptionWhenNoConfigProvided() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> HttpServer.create((HttpServerConfig) null));
    }

    @Test
    void connectionSubmittedToExecutorServiceWhenConnectionEstablished() throws IOException {
        when(configMock.executorService()).thenReturn(executorServiceMock);
        when(configMock.connectionManager()).thenReturn(connectionManagerMock);
        when(configMock.port()).thenReturn(PORT);
        HttpServer httpServer = HttpServer.create(configMock);
        httpServer.start();
        Socket socket = new Socket(InetAddress.getLocalHost(), PORT);
        Mockito.verify(executorServiceMock).submit(any(Thread.class));
        httpServer.interrupt();
    }
}
