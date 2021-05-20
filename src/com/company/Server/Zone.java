package com.company.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author lekeping
 */
public class Zone extends Thread {
    int port;
    ArrayList<UserWorker> userWorkers = new ArrayList<>();

    public Zone(int port) {
        this.port = port;
    }


    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                //starting to accept connection
                System.out.println("start accepting connection");
                Socket socket = serverSocket.accept();
                UserWorker userWorker = new UserWorker(socket, this);
                userWorkers.add(userWorker);
                userWorker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserWorker> getUserWorkers() {
        return userWorkers;
    }
}
