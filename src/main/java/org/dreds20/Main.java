package org.dreds20;

import org.dreds20.httpserver.HttpServer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try (HttpServer httpServer = new HttpServer()) {
            httpServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        try (DataBase dataBase = new DataBase()){
//            Connection connect = dataBase.connect();
//            System.out.println("STOP!");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}