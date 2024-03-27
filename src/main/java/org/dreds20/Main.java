package org.dreds20;

import org.dreds20.config.Config;
import org.dreds20.db.DataBase;
import org.dreds20.db.DataBaseConfig;
import org.dreds20.httpserver.HttpServer;
import org.dreds20.httpserver.SimpleConnectionManager;
import org.dreds20.httpserver.pages.DatabasePageManager;
import org.dreds20.httpserver.pages.FileContentLoader;

import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        HttpServer httpServer = HttpServer.create(builder -> builder
                .executorService(Executors.newVirtualThreadPerTaskExecutor())
                .connectionManager(new SimpleConnectionManager(
                        new DatabasePageManager(new DataBase(DataBaseConfig.create(dbConfigBuilder -> dbConfigBuilder
                                .url(Config.getString("db.url"))
                                .username(Config.getString("db.username"))
                                .password(Config.getString("db.password")))))
                        , new FileContentLoader())));
        httpServer.start();
    }
}