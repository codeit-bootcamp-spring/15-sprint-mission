package com.sprint.mission.discodeit.entity;

public class User extends Common {
    String user;

    public User(String user) {
        super();
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
