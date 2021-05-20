package com.company.broadcast_server;

import java.util.ArrayList;

public class HistoryQue {
    ArrayList<Message> messages = new ArrayList<>();
    int count = 0;
    private Node head;
    private Node tail;
    private boolean ready = true;

    public void addHistory(Message message) {
        if (head == null) {
            head = new Node(message);
            tail = head;
            count++;
            messages.add(head.getMessage());
        } else if (count < 50) {
            Node oldHead = head;
            head = new Node(message);
            head.setNext(oldHead);
            oldHead.setPre(head);
            messages.add(head.getMessage());
            count++;
        } else {
            ready = true;
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
        if (count > 10) {
            return messages;
        } else {
            return null;
        }
    }

}
