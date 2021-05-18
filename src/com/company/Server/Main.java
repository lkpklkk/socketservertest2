package com.company.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        int port = 5000;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                //starting to accept connection
                System.out.println("start accepting connection");
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
