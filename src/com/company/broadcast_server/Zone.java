package com.company.broadcast_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author lekeping
 */
public class Zone extends Thread {
    private int port;
    private ArrayList<UserWorker> userWorkers = new ArrayList<>();
    private HistoryQue historyQue = new HistoryQue();
    public Zone(int port) {
        this.port = port;
    }

    public void addHistory(Message message){
        historyQue.addHistory(message);
    }
    public ArrayList<Message> getHistory(){
     return historyQue.getHistory();

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
                this.userWorkers.add(userWorker);
                userWorker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserWorker> getUserWorkers() {
        return this.userWorkers;
    }

    public void removeWorker(UserWorker userWorker) {
        this.userWorkers.remove(userWorker);
    }
}
