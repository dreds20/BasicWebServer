package org.dreds20.httpserver.model;

import org.dreds20.utils.ImmutableDefault;
import org.immutables.value.Value;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * An HttpResponse object, contains properties to represent components of a valid HttpResponse
 */
@Value.Immutable
@ImmutableDefault
public abstract class HttpResponse {
    public static HttpResponseImpl.Builder builder() {
        return HttpResponseImpl.builder();
    }

    public static HttpResponseImpl.Builder builder(UnaryOperator<HttpResponseImpl.Builder> spec) {
        return spec.apply(builder());
    }

    public static HttpResponse create(UnaryOperator<HttpResponseImpl.Builder> spec) {
        return builder(spec).build();
    }

    /**
     * The HTTP version used for the response
     *
     * @return String - The HTTP version used for the response
     */
    @Value.Default
    public String version() {
        // I know HTTP/2 is the new standard, but this is just a basic WebServer implementation currently
        return "HTTP/1.1";
    }

    /**
     * The HTTP status code of the response
     *
     * @return HttpStatus - The status code of the response
     */
    @Value.Parameter
    public abstract HttpStatus statusCode();

    /**
     * The HTTP headers for the response
     *
     * @return Map<String, String> - The headers of the response
     */
    @Value.Parameter
    public abstract Map<String, String> headers();

    /**
     * The body of the response
     *
     * @return List<String> - The body of the response
     */
    @Value.Parameter
    public abstract List<String> body();

    /**
     * Parses the HttpResponse object to a String
     *
     * @return A Http Response object formatted correctly as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(version()).append(" ").append(statusCode().toString());
        headers().forEach((key, value) -> sb.append("\r\n").append(key).append(": ").append(value));
        if (!body().isEmpty()) {
            StringBuilder bodySB = new StringBuilder();
            body().forEach(line -> bodySB.append(line));
            String encodedBody = new String(bodySB.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            int contentLength = encodedBody.getBytes().length;
            sb.append("\r\n")
                    .append("Content-Length: ")
                    .append(contentLength)
                    .append("\r\n\r\n")
                    .append(encodedBody);
        }
        return sb.toString();
    }

    private static void parseFistLine(String firstLine, HttpResponseImpl.Builder builder) {
        String[] tokens = firstLine.split(" ");
        if (tokens.length < 3) {
            throw new IllegalStateException("First line of response string has insufficient values: " + firstLine);
        }
        builder.version(tokens[0]);
        builder.statusCode(HttpStatus.fromCode(Integer.parseInt(tokens[1])));
    }

    /**
     * A static factory method that will parse a String representation of an HttpResponse into an HttpResponse object
     *
     * @param response the raw response in String form to be parsed to an HttpResponse object
     *
     * @return An instance of HttpResponse parsed from the provided raw String
     */
    public static HttpResponse from(String response) {
        if (response == null) {
            throw new NullPointerException("Cannot parse null string to Http response");
        }
        HttpResponseImpl.Builder builder = HttpResponse.builder();
        List<String> responseList = List.of(response.split("\n"));
        if (!responseList.isEmpty()) {
            parseFistLine(responseList.getFirst(), builder);
        }

        boolean emptyLine = false;
        for (int i = 1; i < responseList.size(); i++) {
            if (responseList.get(i).isEmpty() && !emptyLine) {
                emptyLine = true;
                continue;
            } else if (emptyLine) {
                builder.addBody(responseList.get(i));
                continue;
            }
            String[] keyPair = responseList.get(i).split(":", 2);
            builder.putHeader(keyPair[0].trim(), keyPair[1].trim());
        }
        return builder.build();
    }
}
