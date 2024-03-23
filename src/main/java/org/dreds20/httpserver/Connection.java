package org.dreds20.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dreds20.httpserver.model.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The Connection class extends from the Thread class, and is responsible for
 * handling a connection to the HTTP server, performing a lookup of the request
 * provided, returning a response, and closing the connection.
 */
public class Connection extends Thread {
    private static final Logger log = LogManager.getLogger(Connection.class);
    private final Socket socket;
    private final ConnectionManager connectionManager;

    public Connection(Socket socket, ConnectionManager connectionManager) {
        this.socket = socket;
        this.connectionManager = connectionManager;
    }

    private HttpRequest extractRequest(BufferedReader in) throws IOException {
        List<String> rawRequest = new ArrayList<>();
        String inputLine;
        while (in.ready() && (inputLine = in.readLine()) != null) {
            log.trace("Line: {}", inputLine);
            rawRequest.add(inputLine);
        }

        return HttpRequest.from(rawRequest);
    }

    @Override
    public void run() {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream())) {
            log.debug("Managing connection to server..");

            HttpRequest request = extractRequest(in);
            log.debug("Request received: {}", request);

            String response = connectionManager.getResponse(request);
            log.debug("Dispatching response: {}", response);

            out.print(response);

            log.debug("Response dispatched..");

        } catch (IOException e) {
            log.error("Error thrown while attempting to generate response", e);
        }
    }
}
