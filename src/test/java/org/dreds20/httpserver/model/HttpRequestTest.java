package org.dreds20.httpserver.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.dreds20.httpserver.model.HttpVerb.GET;

public class HttpRequestTest {
    private static final String RAW_REQUEST = "GET / HTTP/1.1\n" +
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
            "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8";
    private static HttpRequest HTTP_REQUEST;

    @BeforeAll
    static void setup() {
        HTTP_REQUEST = HttpRequest.from(List.of(RAW_REQUEST.split("\n")));
    }

    @Test
    void verbIsCorrect() {
        assertThat(HTTP_REQUEST.verb()).isEqualTo(GET);
    }

    @Test
    void uriIsCorrect() {
        assertThat(HTTP_REQUEST.path()).isEqualTo(URI.create("/"));
    }

    @Test
    void versionIsCorrect() {
        assertThat(HTTP_REQUEST.version()).isEqualTo("HTTP/1.1");
    }

    @Test
    void headersContainHost() {
        assertThat(HTTP_REQUEST.headers().get("Host")).isEqualTo("127.0.0.1");
    }

    @Test
    void emptyBody() {
        assertThat(HTTP_REQUEST.body().get()).isEqualTo("");
    }

    @Test
    void requestWithBody() {
        String body = """
                line1
                line2""";
        String additional = """


        """ + body;
        HttpRequest request = HttpRequest.from(List.of((RAW_REQUEST + additional).split("\n")));
        assertThat(request.body().get()).isEqualTo(body);
    }

    @Test
    void expectedVerbIsCreatedFromRawRequest() {
        HttpRequest request = HttpRequest.from(RAW_REQUEST);
        assertThat(request.verb()).isEqualTo(GET);
    }

    @Test
    void emptyStringThrowsException() {
        assertThatThrownBy(() -> HttpRequest.from("")).isInstanceOf(HttpRequestParseException.class);
    }

    @Test
    void nullStringThrowsException() {
        String nullString = null;
        assertThatThrownBy(() -> HttpRequest.from(nullString)).isInstanceOf(HttpRequestParseException.class);
    }

    @Test
    void nullListThrowsException() {
        List<String> nullList = null;
        assertThatThrownBy(() -> HttpRequest.from(nullList)).isInstanceOf(HttpRequestParseException.class);
    }

    @Test
    void firstLineOfRequestIsIncomplete() {
        String firstLine = "GET / ";
        assertThatThrownBy(() -> HttpRequest.from(firstLine)).isInstanceOf(HttpRequestParseException.class);
    }

    @Test
    void firstLineContainsInvalidUri() {
        String firstLine = "GET \\invalid  HTTP/1.1";
        assertThatThrownBy(() -> HttpRequest.from(firstLine)).isInstanceOf(HttpRequestParseException.class);
    }
}
