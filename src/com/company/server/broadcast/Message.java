package com.company.server.broadcast;


/**
 * @author lekeping
 */
public class Message {
    private final int userId;
    String name;
    String content;

    public Message(int userId, String name, String content) {
        this.name = name;
        this.content = content;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("from : %s -- %s\n", name, content);
    }
}


