package com.sprint.mission.discodeit.entity;

public class User extends Common {
    private String userName;
    private String email;
    private String password;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void update(String userName, String email, String password) {
        if (userName != null) {
            this.userName = userName;
        }

        if (email != null) {
            this.email = email;
        }

        if (password != null) {
            this.password = password;
        }

        updateUpdatedAt();
    }
}
