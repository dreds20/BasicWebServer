package org.dreds20.httpserver.model;

import org.immutables.value.Value;
import org.dreds20.utils.ImmutableDefault;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(version()).append(" ").append(statusCode().toString());
        headers().forEach((key, value) -> sb.append("\n").append(key).append(": ").append(value));
        if (!body().isEmpty()) {
            sb.append("\n");
            body().forEach(line -> sb.append("\n").append(line));
        }
        return sb.toString();
    }
}
