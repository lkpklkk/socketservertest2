package com.company.server;

import com.company.server.broadcast_modules.BroadCastTimedTask;
import com.company.server.broadcast_modules.HistoryStorage;
import com.company.server.broadcast_modules.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * @author lekeping
 */
public class Zone extends Thread {
    public static final int BROADCAST_PERIOD = 1000;
    private final Timer timer = new Timer();
    private final Object msgsQueLock = new Object();
    private final int port;
    private ArrayList<Message> msgsQue = new ArrayList<>();
    private final Dao dao;

    public Zone(int port) {
        this.port = port;
        dao = new Dao();
    }

    public void addHistory(Message message) {
        synchronized (dao) {
            dao.addHistory(message);
        }

    }

    public List<Message> getHistory() {
        return dao.getHistory();
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
                dao.addUserWorkers(userWorker);
                userWorker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWorker(UserWorker userWorker) {
        dao.removeUser(userWorker);
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
        try {
            for (UserWorker userWorker : dao.getUserWorkers()) {
                userWorker.write("Broadcasting :\n");
                for (Message message : curBroadCasting) {
                    userWorker.write(message.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
