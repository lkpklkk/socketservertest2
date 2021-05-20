package com.company.broadcast_server;

public class Main {

    public static void main(String[] args) {

        int port = 5000;
        Zone zone = new Zone(port);
        zone.start();


    }
}
