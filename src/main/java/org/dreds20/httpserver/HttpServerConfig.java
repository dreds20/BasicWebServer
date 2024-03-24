package org.dreds20.httpserver;

import org.dreds20.utils.ImmutableDefault;
import org.immutables.value.Value;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;

/**
 * This is a configuration object used for creating an HttpServer. It allows the configuration of the port
 * that the server will listen on, and a ConnectionManager object that will be responsible for handling the
 * request to the server
 */
@Value.Immutable
@ImmutableDefault
public interface HttpServerConfig {
    /**
     * The port the HttpServer will listen on for incoming requests
     *
     * @return an integer representing the port an HttpServer will listen on, the default is 80
     */
    @Value.Default
    default int port() {
        return 80;
    };

    /**
     *
     * @return
     */
    SimpleConnectionManager connectionManager();
    @Value.Default
    default ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    };

    static HttpServerConfigImpl.Builder builder() {
        return HttpServerConfigImpl.builder();
    }

    static HttpServerConfigImpl.Builder builder(UnaryOperator<HttpServerConfigImpl.Builder> spec) {
        return spec.apply(builder());
    }

    static HttpServerConfig create(UnaryOperator<HttpServerConfigImpl.Builder> spec) {
        return builder(spec).build();
    }
}
