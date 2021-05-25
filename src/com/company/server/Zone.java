package com.company.server;

import com.company.server.broadcast.BroadCastTimedTask;
import com.company.server.broadcast.Message;
import com.company.server.mute.MuteDuration;
import com.company.server.mute.UnMuteTimedTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author lekeping
 */
public class Zone extends Thread {
    public static final int BROADCAST_PERIOD = 5000;
    private final Timer timer = new Timer();
    private final Object msgsQueLock = new Object();
    private final int port;
    private final Dao dao;
    private ArrayList<Message> msgsQue = new ArrayList<>();

    public Zone(int port) {
        this.port = port;
        dao = new Dao();
    }

    public void addHistory(Message message) {
        synchronized (dao) {
            dao.addHistory(message);
        }

    }

    public boolean muteUser(MuteDuration muteDuration, int userId) {
        Map<Integer, Boolean> muteMap = dao.getMuteMap();
        if (muteDuration == null || userId == -1 || !muteMap.containsKey(userId)) {
            return false;
        }
        TimerTask task;
        Timer timer = new Timer();

        task = new UnMuteTimedTask(muteMap, userId);
        Date delay;

        switch (muteDuration) {
            default:
                return false;
            case INFINITE:
                muteMap.put(userId, true);
                return true;
            case SEVENDAY:
                muteMap.put(userId, true);
                timer.schedule(task, 1000 * 60 * 60 * 24 * 7);
                return true;
            case ONEDAY:
                muteMap.put(userId, true);
                timer.schedule(task, 1000 * 60 * 60 * 24 * 1);
                return true;
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
            int userId = 0;
            while (true) {
                //starting to accept connection
                System.out.println("start accepting connection");
                Socket socket = serverSocket.accept();
                User user = new User(userId, socket, this);
                System.out.printf("user %d is added\n", userId);
                dao.addUserWorkers(user);
                user.start();
                userId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWorker(User user) {
        dao.removeUser(user);
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
            for (User user : dao.getUserWorkers()) {
                for (Message message : curBroadCasting) {
                    if (message.getUserId() != user.getUserId()) {
                        user.write(message.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Iterable<? extends User> getUsers() {
        return dao.getUserWorkers();
    }

    public boolean muteAuthenticate(int userId) {
        return dao.muteAuthenticate(userId);
    }
}
