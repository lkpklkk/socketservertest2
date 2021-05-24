package com.company.server.broadcast_modules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lekeping
 */
public class HistoryStorage {
    public static final int MAX_RECENT_HISTORY = 50;
    public static final int MIN_HISTORY_DISPLAY = 1;
    List<Message> messages = new LinkedList<>();
    int count = 0;

    public void addHistory(Message message) {
        if (count < MAX_RECENT_HISTORY) {
            messages.add(message);
        } else {
            messages.add(message);
            messages.remove(0);
        }
    }

    public List<Message> getHistory() {
        if (count >= MIN_HISTORY_DISPLAY) {
            return messages;
        } else {
            return null;
        }
    }

}
