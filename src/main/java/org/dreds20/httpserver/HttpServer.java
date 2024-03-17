package org.dreds20.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class HttpServer implements AutoCloseable {
    private boolean running = false;

    public void start() {
        running = true;
        while (running) {
            try (ServerSocket socket = new ServerSocket(80);){
                Socket clientSocket = socket.accept();
                System.out.println(String.join("\n", new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).lines().collect(Collectors.toList())));
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
