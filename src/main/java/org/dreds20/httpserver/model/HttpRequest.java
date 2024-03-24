package org.dreds20.httpserver.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.immutables.value.Value;
import org.dreds20.utils.ImmutableDefault;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * An object representation of an HttpRequest
 */
@Value.Immutable
@ImmutableDefault
public interface HttpRequest {
    static final Logger log = LogManager.getLogger(HttpRequest.class);
    static HttpRequestImpl.Builder builder() {
        return HttpRequestImpl.builder();
    }

    static HttpRequestImpl.Builder builder(UnaryOperator<HttpRequestImpl.Builder> spec) {
        return spec.apply(builder());
    }

    static HttpRequest create(UnaryOperator<HttpRequestImpl.Builder> spec) {
        return spec.apply(builder()).build();
    }

    /**
     * HTTP verb used in a request, e.g. PUT, POST etc.
     *
     * @return HttpVerb - The HTTP verb used in a response
     */
    HttpVerb verb();

    /**
     * The URI requested in the HTTP request
     *
     * @return URI - The URI requested in the HTTP request
     */
    URI path();

    /**
     * The version of the HTTP request, e.g. HTTP/1.0
     *
     * @return String - The version of the HTTP request
     */
    String version();

    /**
     * Key value pairs of all the headers used in the HTTP request
     *
     * @return Map<String, String> - A list of headers provided in the HTTP request
     */
    Map<String, String> headers();

    /**
     * The body sent in the Http request
     *
     * @return String - The body of the HTTP request
     */

    Optional<String> body();


    private static void parseFirstLine(HttpRequestImpl.Builder builder, String firstLine) {
        String[] fistLine = firstLine.split(" ");
        if (fistLine.length < 3) {
            throw new HttpRequestParseException("First line should contain three values, but contains " + fistLine.length);
        }

        builder.verb(HttpVerb.valueOf(fistLine[0].toUpperCase(Locale.getDefault())));

        try {
            builder.path(new URI(fistLine[1]));
        } catch (URISyntaxException e) {
            throw new HttpRequestParseException("Invalid URI provided in HTTP request", e);
        }

        builder.version(fistLine[2]);
    }

    /**
     * Parses a list of Strings into a HttpRequest object
     *
     * @param rawRequest A list of Strings containing the raw HttpRequest
     *
     * @return an HttpRequest object parsed from the list of Strings provided
     */
    static HttpRequest from(List<String> rawRequest) {
        if (rawRequest == null || rawRequest.isEmpty()) {
            throw new HttpRequestParseException("Raw request provided is null or empty");
        }
        HttpRequestImpl.Builder builder = builder();
        parseFirstLine(builder, rawRequest.getFirst());

        boolean emptyLine = false;
        List<String> body = new ArrayList<>();
        Map<String, String> headers = new HashMap<>();

        for (int i = 1; i < rawRequest.size(); i++) {
            String currentLine = rawRequest.get(i);
            if (currentLine.isEmpty() && !emptyLine) {
                emptyLine = true;
                continue;
            } else if (emptyLine) {
                body.add(currentLine);
                continue;
            }
            String[] keyPair = currentLine.split(":", 2);
            headers.put(keyPair[0].trim(), keyPair[1].trim());
        }

        builder.headers(headers)
                .body(String.join("\n", body));
        return builder.build();
    }

    /**
     * Parses a String into a HttpRequest object
     *
     * @param rawRequest A String containing the raw HttpRequest
     *
     * @return an HttpRequest object parsed from the String provided
     */
    static HttpRequest from(String rawRequest) {
        if (rawRequest == null || rawRequest.isEmpty()) {
            throw new HttpRequestParseException("Raw request provided is null or empty");
        }
        return from(List.of(rawRequest.split("\n")));
    }
}
