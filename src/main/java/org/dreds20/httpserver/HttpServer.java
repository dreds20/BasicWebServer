package org.dreds20.httpserver;

import org.dreds20.httpserver.pages.ContentLoader;
import org.dreds20.httpserver.pages.DatabasePageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class HttpServer implements AutoCloseable {
    private boolean running = false;

    public void start() {
        running = true;
        ConnectionManager connectionManager = new ConnectionManager(new DatabasePageManager(), new ContentLoader());
        while (running) {
            try (ServerSocket socket = new ServerSocket(80);
                 ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())){
                executorService.submit(new Connection(socket.accept(), connectionManager));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        running = false;
    }
}
