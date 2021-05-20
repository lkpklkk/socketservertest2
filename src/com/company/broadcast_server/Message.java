package com.company.broadcast_server;


public class Message {
    String name;
    String content;

    public Message(String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("from : %s -- %s\n", name, content);
    }
}


