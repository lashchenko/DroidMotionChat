package com.dm;

public class User {

    private String username;

    public User() {
        this(android.os.Build.MODEL);
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
