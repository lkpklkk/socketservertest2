package com.company.Server;

public class Main {

    public static void main(String[] args) {

        int port = 5000;
        Zone zone = new Zone(port);
        zone.start();


    }
}
