package org.dreds20.httpserver;

public class HttpServerRuntimeException extends RuntimeException {
    public HttpServerRuntimeException() {
        super();
    }

    public HttpServerRuntimeException(String message) {
        super(message);
    }

    public HttpServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpServerRuntimeException(Throwable cause) {
        super(cause);
    }
}
