package org.dreds20.httpserver.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class HttpResponseTest {

    @Test
    public void exceptionThrownWhenAttemptingToBuildWithoutStatusCode() {
        assertThatThrownBy(() -> HttpResponse.builder().version("HTTP/1.0").build()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void defaultVersion() {
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).build().toString()).isEqualTo("HTTP/1.1 200 OK");
    }

    @Test
    public void onlyVersionAndStatus() {
        HttpResponse response = HttpResponse.builder().version("HTTP/1.0").statusCode(HttpStatus.OK).build();
        assertThat(response.toString()).isEqualTo("HTTP/1.0 200 OK");
    }

    @Test
    public void headersAreIncluded() {
        Map<String, String> headers = Map.of("Content-Type", "text/html");
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).headers(headers).build().toString()).isEqualTo("HTTP/1.1 200 OK\n" +
                "Content-Type: text/html");
    }

    @Test
    public void bodyIsIncluded() {
        String body = "Test body";
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).body(List.of(body)).build().toString()).isEqualTo("HTTP/1.1 200 OK\n" +
                "\n" + body);
    }

    @Test
    public void bodyAndHeadersAreIncluded() {
        String body = "Test body";
        Map<String, String> headers = Map.of("Content-Type", "text/html");
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).headers(headers).body(List.of(body)).build().toString()).isEqualTo("HTTP/1.1 200 OK\n" +
                "Content-Type: text/html\n" +
                "\n" + body);
    }
}
