package org.dreds20;

import org.dreds20.httpserver.ConnectionManager;
import org.dreds20.httpserver.HttpServer;
import org.dreds20.httpserver.pages.ContentLoader;
import org.dreds20.httpserver.pages.DatabasePageManager;

import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        HttpServer httpServer = HttpServer.create(builder -> builder
                .executorService(Executors.newVirtualThreadPerTaskExecutor())
                .connectionManager(new ConnectionManager(
                        new DatabasePageManager(), new ContentLoader())));
        httpServer.start();
    }
}