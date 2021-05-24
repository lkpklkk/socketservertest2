package com.company.server.broadcast_modules;

/**
 * @author lekeping
 */
public class Node {
    private final Message message;
    private Node next = null;
    private Node pre = null;

    public Node getPre() {
        return pre;
    }

    public void setPre(Node pre) {
        this.pre = pre;
    }

    public Node(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
