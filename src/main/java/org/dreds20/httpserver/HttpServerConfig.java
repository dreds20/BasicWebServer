package org.dreds20.httpserver;

import org.dreds20.utils.ImmutableDefault;
import org.immutables.value.Value;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;

@Value.Immutable
@ImmutableDefault
public interface HttpServerConfig {
    @Value.Default
    default int port() {
        return 80;
    };
    ConnectionManager connectionManager();
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
