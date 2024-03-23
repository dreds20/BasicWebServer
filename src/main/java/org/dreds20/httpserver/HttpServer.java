package org.dreds20.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
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

    /**
     * A static creation method for the HTTP server which can utilise a builder of the HttpServerConfig
     * object to create the configuration, e.g.
     * <pre>
     *     {@code HttpServer httpServer = HttpServer.create(builder -> builder
     *                 .executorService(Executors.newVirtualThreadPerTaskExecutor())...}
     * </pre>
     *
     * @param spec This param should be a lambda function representing the builder
     *             that will create the HttpServer Configuration object
     *
     * @return an HttpServer thread which can be executed with the start method
     */
    public static HttpServer create(UnaryOperator<HttpServerConfigImpl.Builder> spec) {
        return new HttpServer(HttpServerConfig.create(spec));
    }

    /**
     * This static create method will construct an instance of an HttpServer with a provided
     * HttpServerConfig object
     *
     * @param config An HttpServerConfig object with all the necessary configuration to instantiate
     *               the HttpServer class
     *
     * @return an HttpServer thread which can be executed with the start method
     */
    public static HttpServer create(HttpServerConfig config) {
        return new HttpServer(config);
    }

    /**
     * This method will start the HttpServer, the expectation is that this would be executed from the
     * start method.
     */
    @Override
    public void run() {
        running = true;

        try (ExecutorService executorService = config.executorService()) {
            SimpleConnectionManager connectionManager = config.connectionManager();
            log.info("Starting http server..");
            while (running) {
                try (ServerSocket socket = new ServerSocket(config.port());){
                    log.debug("Waiting for request..");
                    Socket clientSocket = socket.accept();
                    log.debug("Socket created for '{}'", clientSocket.getRemoteSocketAddress());
                    executorService.submit(new Connection(clientSocket, connectionManager));
                } catch (IOException e) {
                    throw new HttpServerRuntimeException("Exception thrown during HTTP server execution", e);
                }
            }
        }
        log.info("Server stopped..");
    }

    /**
     * An override of the interrupt method which will stop the execution of the Http Server
     */
    @Override
    public void interrupt() {
        log.info("Stopping http server..");
        running = false;
    }
}
