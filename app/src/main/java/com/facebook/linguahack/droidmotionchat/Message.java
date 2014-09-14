package com.facebook.linguahack.droidmotionchat;

public class Message {

    private final String user;
    private final String text;

    public static final Message EMPTY_MESSAGE = new Message("", "");

    public Message(String text, String user) {
        this.text = text;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public String getUser() {
        return user;
    }

    public boolean isEmpty() {
        return this.getText().isEmpty() && this.getUser().isEmpty();
    }

    @Override
    public String toString() {
        return " [" + getUser() + "] " + getText();
    }

}
