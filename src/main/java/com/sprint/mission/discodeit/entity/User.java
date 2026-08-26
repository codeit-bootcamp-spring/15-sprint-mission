package com.sprint.mission.discodeit.entity;

public class User extends Common {
    String user;
    String email;
    String userId;

    public User(String user, String email, String userId) {
        super();
        this.user = user;
        this.email = email;
        this.userId = userId;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
