package org.dreds20.httpserver;

import org.dreds20.httpserver.model.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionTest {
    @Mock
    Socket clientSocketMock;

    @Mock
    ConnectionManager connectionManagerMock;

    @Mock
    InputStream inputStreamMock;

    @Mock
    OutputStream outputStreamMock;

    private static String REQUEST = "GET / HTTP/1.1\n" +
            "Host: 127.0.0.1\n" +
            "Connection: keep-alive\n" +
            "sec-ch-ua: \"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"\n" +
            "sec-ch-ua-mobile: ?0\n" +
            "sec-ch-ua-platform: \"Windows\"\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n" +
            "Sec-Fetch-Site: none\n" +
            "Sec-Fetch-Mode: navigate\n" +
            "Sec-Fetch-User: ?1\n" +
            "Sec-Fetch-Dest: document\n" +
            "Accept-Encoding: gzip, deflate, br, zstd\n" +
            "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8\n";

    @BeforeEach
    void beforeEach() throws IOException {
        when(clientSocketMock.getInputStream()).thenReturn(new ByteArrayInputStream(REQUEST.getBytes(StandardCharsets.UTF_8)));
        when(clientSocketMock.getOutputStream()).thenReturn(outputStreamMock);
    }

    @Test
    void expectedRequestSentToConnectionManager() {
        Connection connection = new Connection(clientSocketMock, connectionManagerMock);
        connection.run();
        verify(connectionManagerMock).getResponse(eq(HttpRequest.from(REQUEST)));
    }

    @Test
    void resultSentToOutputStream() throws IOException {
        Connection connection = new Connection(clientSocketMock, connectionManagerMock);
        connection.run();
        verify(outputStreamMock).write(any(byte[].class), anyInt(), anyInt());
    }

}
