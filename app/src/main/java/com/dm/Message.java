package com.dm;

public class Message {
    private final String text;
    private final User user;

    public Message(String text, User user) {
        this.text = text;
        this.user = user;
    }
}
