package org.dreds20.httpserver;

import org.dreds20.config.Config;
import org.dreds20.httpserver.model.HttpRequest;
import org.dreds20.httpserver.model.HttpResponse;
import org.dreds20.httpserver.model.HttpStatus;
import org.dreds20.httpserver.model.HttpVerb;
import org.dreds20.httpserver.pages.ContentLoader;
import org.dreds20.httpserver.pages.Page;
import org.dreds20.httpserver.pages.PageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimpleConnectionManagerTest {
    @Mock PageManager pageManagerMock;
    @Mock ContentLoader contentLoaderMock;
    @Mock Page pageMock;
    SimpleConnectionManager connectionManager;
    HttpRequest request = HttpRequest.create(builder -> builder.path(URI.create("/")).verb(HttpVerb.GET).version("HTTP/1.1"));

    private void setupPageMock(HttpVerb verb, String path) {
        when(pageMock.verbs()).thenReturn(List.of(verb));
        when(pageMock.path()).thenReturn(path);
        when(pageMock.contentType()).thenReturn("text/html");
    }

    @BeforeEach
    void beforeEach() {
        connectionManager = new SimpleConnectionManager(pageManagerMock, contentLoaderMock);
    }

    @Test
    void nullPageManagerThrowsException() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new SimpleConnectionManager(null, contentLoaderMock));
    }

    @Test
    void nullContentLoaderThrowsException() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new SimpleConnectionManager(pageManagerMock, null));
    }

    @Test
    void internalServerFailureWhenExceptionThrownRetrievingPage() throws Exception {
        when(pageManagerMock.getPage("/")).thenThrow(RuntimeException.class);
        assertThat(connectionManager.getResponse(request)).isEqualTo(HttpResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Test
    void connectionManagerReturnsNullPageNotFoundReturned() throws Exception {
        when(pageManagerMock.getPage("/")).thenReturn(null);
        assertThat(connectionManager.getResponse(request).statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void httpVerbNotSpecifiedOnReturnedPage() throws Exception {
        when(pageManagerMock.getPage("/")).thenReturn(pageMock);
        when(pageMock.verbs()).thenReturn(List.of(HttpVerb.POST));
        assertThat(connectionManager.getResponse(request).statusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    void contentLoaderCantLoadHtmlFile() throws Exception {
        String path = "/path/index.html";
        String basePath = "/start";
        Config.get().setProperty("pages.basePath", basePath);
        when(pageManagerMock.getPage("/")).thenReturn(pageMock);
        when(pageMock.verbs()).thenReturn(List.of(HttpVerb.GET));
        setupPageMock(HttpVerb.GET, path);
        when(contentLoaderMock.getContent(Paths.get(basePath, path).toUri())).thenThrow(new IOException());
        assertThat(connectionManager.getResponse(request)).isEqualTo(HttpResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Test
    void successfulResponseSentStatusCodeIsCorrect() throws Exception {
        String path = "/path/index.html";
        List<String> body = List.of("<head>", "</head>", "<body>","<h1>Title</h1>","/body>");
        Config.get().setProperty("pages.basePath", "");
        when(pageManagerMock.getPage("/")).thenReturn(pageMock);
        when(pageMock.verbs()).thenReturn(List.of(HttpVerb.GET));
        setupPageMock(HttpVerb.GET, path);
        when(contentLoaderMock.getContent(Paths.get(path).toAbsolutePath().toUri())).thenReturn(body);
        assertThat(connectionManager.getResponse(request).statusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void successfulResponseSentContentTypeIsCorrect() throws Exception {
        String path = "/path/index.html";
        List<String> body = List.of("<head>", "</head>", "<body>","<h1>Title</h1>","/body>");
        Config.get().setProperty("pages.basePath", "");
        when(pageManagerMock.getPage("/")).thenReturn(pageMock);
        when(pageMock.verbs()).thenReturn(List.of(HttpVerb.GET));
        setupPageMock(HttpVerb.GET, path);
        when(contentLoaderMock.getContent(Paths.get(path).toAbsolutePath().toUri())).thenReturn(body);
        assertThat(connectionManager.getResponse(request).headers().get("Content-Type"))
                .isEqualTo("text/html");
    }

    @Test
    void successfulResponseSentBodyIsCorrect() throws Exception {
        String path = "/path/index.html";
        List<String> body = List.of("<head>", "</head>", "<body>","<h1>Title</h1>","/body>");
        Config.get().setProperty("pages.basePath", "");
        when(pageManagerMock.getPage("/")).thenReturn(pageMock);
        when(pageMock.verbs()).thenReturn(List.of(HttpVerb.GET));
        setupPageMock(HttpVerb.GET, path);
        when(contentLoaderMock.getContent(Paths.get(path).toAbsolutePath().toUri())).thenReturn(body);
        assertThat(connectionManager.getResponse(request).body())
                .isEqualTo(body);
    }
}
