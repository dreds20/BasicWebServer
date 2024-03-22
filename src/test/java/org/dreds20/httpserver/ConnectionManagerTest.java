package org.dreds20.httpserver;

import org.assertj.core.api.Assertions;
import org.dreds20.httpserver.model.HttpRequest;
import org.dreds20.httpserver.model.HttpResponse;
import org.dreds20.httpserver.model.HttpStatus;
import org.dreds20.httpserver.model.HttpVerb;
import org.dreds20.httpserver.pages.ContentLoader;
import org.dreds20.httpserver.pages.PageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionManagerTest {
    @Mock
    PageManager pageManagerMock;
    @Mock
    ContentLoader contentLoaderMock;

    ConnectionManager connectionManager;
    HttpRequest request = HttpRequest.create(builder -> builder.path(URI.create("/")).verb(HttpVerb.GET).version("HTTP/1.1"));

    @BeforeEach
    void beforeEach() {
        connectionManager = new ConnectionManager(pageManagerMock, contentLoaderMock);
    }

    @Test
    void internalServerFailureWhenExceptionThrownRetrievingPage() throws Exception {
        when(pageManagerMock.getPage("/")).thenThrow(RuntimeException.class);
        assertThat(connectionManager.getResponse(request)).isEqualTo(HttpResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR).build().toString());
    }

    @Test
    void connectionManagerReturnsNullPageNotFoundReturned() throws Exception {
        when(pageManagerMock.getPage("/")).thenReturn(null);
        assertThat(HttpResponse.from(connectionManager.getResponse(request)).statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
