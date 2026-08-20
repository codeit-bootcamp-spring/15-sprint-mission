package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {

    private String username;
    private String email;
    private String password;


    public User(String username, String email, String password)
    {
        super();
        this.username = username;
        this.email = email;
        this.password = this.password;
    }

    public void update(String username, String email, String password)
    {
        this.username =username;
        this.email = email;
        this.password = password;
        touch();
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    //+@ 추가할거
}
