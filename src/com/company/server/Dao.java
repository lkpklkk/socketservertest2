package com.company.server;

import com.company.server.broadcast_modules.HistoryStorage;
import com.company.server.broadcast_modules.Message;
import com.sun.deploy.net.MessageHeader;

import java.util.ArrayList;
import java.util.List;

public class Dao {
    private final HistoryStorage historyStorage = new HistoryStorage();
    private final ArrayList<UserWorker> userWorkers = new ArrayList<>();

    public void addUserWorkers(UserWorker userWorker) {
        userWorkers.add(userWorker);
    }


    public void addHistory(Message message) {
        historyStorage.addHistory(message);
    }

    public List<Message> getHistory() {
        return historyStorage.getHistory();
    }

    public void removeUser(UserWorker userWorker) {
        userWorkers.remove(userWorker);
    }

    public Iterable<? extends UserWorker> getUserWorkers() {
        return userWorkers;
    }
}
