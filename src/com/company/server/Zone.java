package com.company.server;

import com.company.server.broadcast_modules.BroadCastTimedTask;
import com.company.server.broadcast_modules.HistoryStorage;
import com.company.server.broadcast_modules.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

/**
 * @author lekeping
 */
public class Zone extends Thread {
    public static final int BROADCAST_PERIOD = 1000;
    private final Timer timer = new Timer();
    private final Object msgsQueLock = new Object();
    private final Object historyLock = new Object();
    private final int port;
    private final ArrayList<UserWorker> userWorkers = new ArrayList<>();
    private ArrayList<Message> msgsQue = new ArrayList<>();
    private final HistoryStorage historyStorage = new HistoryStorage();

    public Zone(int port) {
        this.port = port;
    }

    public void addHistory(Message message) {
        historyStorage.addHistory(message);
    }

    public ArrayList<Message> getHistory() {
        synchronized (historyLock) {
            return historyStorage.getHistory();
        }
    }

    @Override
    public void run() {
        timer.scheduleAtFixedRate(new BroadCastTimedTask(this), 0, BROADCAST_PERIOD);
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

    public void removeWorker(UserWorker userWorker) {
        this.userWorkers.remove(userWorker);
    }


    public void addMsgsQue(Message message) {
        synchronized (msgsQueLock) {
            msgsQue.add(message);
        }
    }

    public ArrayList<Message> getMsgsQueAndClear() {
        ArrayList<Message> res = msgsQue;
        synchronized (msgsQueLock) {
            msgsQue = new ArrayList<>();
        }
        return res;
    }

    public void broadcast(ArrayList<Message> curBroadCasting) {
        for (UserWorker userWorker : userWorkers) {
            for (Message message : curBroadCasting) {
                try {
                    userWorker.write(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
