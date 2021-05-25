package com.company.server;

import com.company.server.broadcast.BroadCastTimedTask;
import com.company.server.broadcast.Message;
import com.company.server.mute.MuteDuration;
import com.company.server.mute.UnMuteTimedTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lekeping
 */
public class Zone extends Thread {
    public static final int BROADCAST_PERIOD_SEC = 1;
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
    private final Object msgsQueLockObject = new Object();
    private final int port;
    private final Dao dao;
    private ArrayList<Message> msgsQue = new ArrayList<>();

    public Zone(int port) {
        this.port = port;
        dao = new Dao();
    }

    @Override
    public void run() {
        Runnable broadCastTask = new BroadCastTimedTask(this);
        ses.scheduleAtFixedRate(broadCastTask, 0, BROADCAST_PERIOD_SEC, TimeUnit.SECONDS);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            int userId = 0;
            while (true) {
                //starting to accept connection
                System.out.println("start accepting connection");
                Socket socket = serverSocket.accept();
                User user = new User(userId, socket, this);
                System.out.printf("user %d is added\n", userId);
                dao.addUsers(user);
                user.start();
                userId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMsgsQue(Message message) {
        //msgsQueLock ensures no messages would be lost due to unsafe thread operation
        synchronized (msgsQueLockObject) {
            msgsQue.add(message);
        }
    }

    public ArrayList<Message> getMsgsQueAndClear() {
        //msgsQueLock ensures no messages would be lost due to unsafe thread operation
        ArrayList<Message> res = msgsQue;
        synchronized (msgsQueLockObject) {
            msgsQue = new ArrayList<>();
        }
        return res;
    }

    public synchronized void addHistory(Message message) {
        dao.addHistory(message);
    }

    public boolean muteUser(MuteDuration muteDuration, int userId) {
        Map<Integer, Boolean> muteMap = dao.getMuteMap();
        if (muteDuration == null || userId == -1 || !muteMap.containsKey(userId)) {
            return false;
        }
        Runnable task = new UnMuteTimedTask(muteMap, userId);
        switch (muteDuration) {
            default:
                return false;
            case INFINITE:
                muteMap.put(userId, true);
                return true;
            case SEVENDAY:
                muteMap.put(userId, true);
                ses.schedule(task, 7, TimeUnit.DAYS);
                return true;
            case ONEDAY:
                muteMap.put(userId, true);
                ses.schedule(task, 1, TimeUnit.DAYS);
                return true;
        }
    }

    public List<Message> getHistory() {
        return dao.getHistory();
    }

    public void removeUser(User user) {
        dao.removeUser(user);
    }

    public void broadcast(ArrayList<Message> curBroadCasting) {
        try {
            for (User user : dao.getUsers()) {
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
        return dao.getUsers();
    }

    public boolean muteAuthenticate(int userId) {
        return dao.muteAuthenticate(userId);
    }
}
