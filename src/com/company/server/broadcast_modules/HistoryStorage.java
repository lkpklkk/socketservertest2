package com.company.server.broadcast_modules;

import java.util.ArrayList;

/**
 * @author lekeping
 */
public class HistoryStorage {
    public static final int MAX_RECENT_HISTORY = 50;
    public static final int MIN_HISTORY_DISPLAY = 10;
    ArrayList<Message> messages = new ArrayList<>();
    int count = 0;
    private Node head;
    private Node tail;

    public void addHistory(Message message) {
        if (head == null) {
            head = new Node(message);
            tail = head;
            count++;
            messages.add(head.getMessage());
        } else if (count < MAX_RECENT_HISTORY) {
            Node oldHead = head;
            head = new Node(message);
            head.setNext(oldHead);
            oldHead.setPre(head);
            messages.add(head.getMessage());
            count++;
        } else {
            tail = tail.getPre();
            messages.remove(tail.getMessage());
            tail.setNext(null);
            Node oldHead = head;
            head = new Node(message);
            messages.add(head.getMessage());
            head.setNext(oldHead);
            oldHead.setPre(head);

        }
    }

    public ArrayList<Message> getHistory() {
        if (count > MIN_HISTORY_DISPLAY) {
            return messages;
        } else {
            return null;
        }
    }

}