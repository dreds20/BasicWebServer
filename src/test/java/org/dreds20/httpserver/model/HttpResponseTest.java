package org.dreds20.httpserver.model;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class HttpResponseTest {

    @Test
    void exceptionThrownWhenAttemptingToBuildWithoutStatusCode() {
        assertThatThrownBy(() -> HttpResponse.builder().version("HTTP/1.0").build()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void defaultVersion() {
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).build().toString()).isEqualTo("HTTP/1.1 200 OK");
    }

    @Test
    void onlyVersionAndStatus() {
        HttpResponse response = HttpResponse.builder().version("HTTP/1.0").statusCode(HttpStatus.OK).build();
        assertThat(response.toString()).isEqualTo("HTTP/1.0 200 OK");
    }

    @Test
    void headersAreIncluded() {
        Map<String, String> headers = Map.of("Content-Type", "text/html");
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).headers(headers).build().toString()).isEqualTo("HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html");
    }

    @Test
    void bodyIsIncluded() {
        String body = "Test body";
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).body(List.of(body)).build().toString()).isEqualTo("HTTP/1.1 200 OK\r\nContent-Length: 9\r\n" +
                "\r\n" + body);
    }

    @Test
    void bodyAndHeadersAreIncluded() {
        String body = "Test body";
        Map<String, String> headers = Map.of("Content-Type", "text/html");
        assertThat(HttpResponse.builder().statusCode(HttpStatus.OK).headers(headers).body(List.of(body)).build().toString()).isEqualTo("HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\nContent-Length: 9\r\n" +
                "\r\n" + body);
    }

    @Test
    void ResponseCanBeCreatedFromString() {
        String body = "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Index</h1>\n" +
                "\n" + // Adding a new line to make sure the parsing catches this
                "</body>";

        String rawResponse = "HTTP/1.1 200 OK\n" +
                "Date: Fri, 22 Mar 2024 15:35:22 GMT\n" +
                "Content-Type: text/html\n" +
                "\n" + body;

        HttpResponse response = HttpResponse.from(rawResponse);
        SoftAssertions responseBundle = new SoftAssertions();
        responseBundle.assertThat(response.version()).isEqualTo("HTTP/1.1");
        responseBundle.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        responseBundle.assertThat(response.headers().get("Date")).isEqualTo("Fri, 22 Mar 2024 15:35:22 GMT");
        responseBundle.assertThat(response.headers().get("Content-Type")).isEqualTo("text/html");
        responseBundle.assertThat(response.body()).isEqualTo(body.split("\n"));
    }
}
