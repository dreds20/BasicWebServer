package org.dreds20.httpserver.model;

public class HttpRequestParseException extends RuntimeException {
    public HttpRequestParseException() {
        super();
    }

    public HttpRequestParseException(String message) {
        super(message);
    }

    public HttpRequestParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestParseException(Throwable cause) {
        super(cause);
    }
}
