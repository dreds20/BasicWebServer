package org.dreds20.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dreds20.httpserver.pages.ContentLoader;
import org.dreds20.httpserver.pages.DatabasePageManager;
import org.dreds20.utils.ImmutableDefault;
import org.immutables.value.Value;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.UnaryOperator;

/**
 * This class is responsible for running an HTTP server, it extends Thread so can be initialised
 * with the start() method. The server can be configured to be run against any specified TCP port,
 * but it will default to port 80.
 *
 * Ths server will use a provided executor service to run
 */
public class HttpServer extends Thread {
    private static final Logger log = LogManager.getLogger(HttpServer.class.getName());
    private boolean running = false;
    private final HttpServerConfig config;

    private HttpServer(HttpServerConfig config) {
        if (config == null) {
            throw new NullPointerException("Provided configuration is null");
        }
        this.config = config;
    }

    public static HttpServer create(UnaryOperator<HttpServerConfigImpl.Builder> spec) {
        return new HttpServer(HttpServerConfig.create(spec));
    }

    public static HttpServer create(HttpServerConfig config) {
        return new HttpServer(config);
    }

    @Override
    public void run() {
        running = true;

        try (ExecutorService executorService = config.executorService()) {
            ConnectionManager connectionManager = config.connectionManager();
            log.info("Starting http server..");
            while (running) {
                try (ServerSocket socket = new ServerSocket(config.port());){
                    log.info("Waiting for request..");
                    Socket clientSocket = socket.accept();
                    log.debug("Socket created for '{}'", clientSocket.getRemoteSocketAddress());
                    log.info("Request received, submitting to executor service..");
                    executorService.submit(new Connection(clientSocket, connectionManager));
                } catch (IOException e) {
                    throw new HttpServerRuntimeException("Exception thrown during HTTP server execution", e);
                }
            }
        }
    }

    @Override
    public void interrupt() {
        running = false;
    }
}
