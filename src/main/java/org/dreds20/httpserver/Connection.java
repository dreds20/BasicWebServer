package org.dreds20.httpserver;

import org.dreds20.httpserver.model.HttpRequest;
import org.dreds20.httpserver.model.HttpResponse;
import org.dreds20.httpserver.model.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Connection extends Thread {
    private final Socket socket;
    private final ConnectionManager connectionManager;

    public Connection(Socket socket, ConnectionManager connectionManager) {
        this.socket = socket;
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        try {
            List<String> rawRequest = new BufferedReader(new InputStreamReader(socket.getInputStream())).lines().collect(Collectors.toList());
            HttpRequest request = HttpRequest.from(rawRequest);
            String response = connectionManager.getResponse(request);
            socket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
