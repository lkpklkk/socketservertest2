package com.company.server;

import com.company.server.broadcast.HistoryStorage;
import com.company.server.broadcast.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lekeping
 */
public class Dao {
    private final HistoryStorage historyStorage = new HistoryStorage();
    private final ArrayList<User> users = new ArrayList<>();
    private final Map<Integer, Boolean> muteMap = new HashMap<>();

    public void addUserWorkers(User user) {
        users.add(user);
        muteMap.put(user.getUserId(), false);
    }


    public void addHistory(Message message) {
        historyStorage.addHistory(message);
    }

    public List<Message> getHistory() {
        return historyStorage.getHistory();
    }

    public void removeUser(User user) {
        users.remove(user);
        muteMap.remove(user.getUserId());
    }

    public Iterable<? extends User> getUserWorkers() {
        return users;
    }

    public boolean muteAuthenticate(int userId) {
        return !muteMap.get(userId);
    }

    public Map<Integer, Boolean> getMuteMap() {
        return muteMap;
    }
}
