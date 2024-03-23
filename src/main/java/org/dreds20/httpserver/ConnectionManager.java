package org.dreds20.httpserver;

import org.dreds20.httpserver.model.HttpRequest;
import org.dreds20.httpserver.model.HttpResponse;
import org.dreds20.httpserver.model.HttpResponseImpl;
import org.dreds20.httpserver.model.HttpStatus;

/**
 * An interface of the ConnectionManager, the implementation of this should be used with the HttpServer
 * and should be responsible for handling requests dispatched to the HttpServer and providing responses
 */
public interface ConnectionManager {

    HttpResponse getResponse(HttpRequest request);
    String getResponseString(HttpRequest request);
    static HttpResponseImpl.Builder getFailureBuilder() {
        return HttpResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Generates a simple timeout HttpResponse object
     *
     * @return a simple HttpResponse representing a timeout
     */
    public static HttpResponse getTimeoutResponse() {
        return HttpResponse.builder().statusCode(HttpStatus.REQUEST_TIMEOUT).build();
    }

    /**
     * Generates a simple internal server error HttpResponse object
     *
     * @return a simple HttpResponse representing an internal server error
     */
    public static HttpResponse getFailureResponse() {
        return getFailureBuilder().build();
    }
}
