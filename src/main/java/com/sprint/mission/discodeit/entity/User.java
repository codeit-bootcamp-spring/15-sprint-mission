package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseEntity {

    private String username;
    private String email;
    private String password;
    private UUID profileImageId;


    public User(String username, String email, String password, UUID profileImageId)
    {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageId = profileImageId;
    }

    public void update(String username, String email, String password, UUID profileImageId)
    {
        this.username =username;
        this.email = email;
        this.password = password;
        this.profileImageId = profileImageId;
        touch();
    }


}
