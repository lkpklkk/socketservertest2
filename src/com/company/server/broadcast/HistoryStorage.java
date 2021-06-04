package com.company.server.broadcast;

import java.util.LinkedList;
import java.util.List;

/**
 * a history data storage class
 *
 * @author lekeping
 */
public class HistoryStorage {
    public static final int MAX_RECENT_HISTORY = 50;
    public static final int MIN_HISTORY_DISPLAY = 1;
    List<Message> messages = new LinkedList<>();
    int count = 0;

    /**
     * add the message to tail of linked list
     * remove head of linked list if capacity reaches MAX_RECENT_HISTORY
     *
     * @param message incoming message
     */
    public void addHistory(Message message) {
        messages.add(message);
        if (count < MAX_RECENT_HISTORY) {

            count++;
        } else {


            messages.remove(0);
        }
    }

    /**
     * @return null if server just starts, otherwise returns chat history
     */
    public List<Message> getHistory() {
        if (count >= MIN_HISTORY_DISPLAY) {
            return messages;
        } else {
            return null;
        }
    }

}
