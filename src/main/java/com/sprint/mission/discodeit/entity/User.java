package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.util.UUID;

//계정,비번,닉,등급?

@Getter
public class User extends BaseClass {
    private String email;
    private String password;
    private String username;
    private UUID profileId;


    public User(String email, String password, String username, UUID profileId) {
        super();
        this.email=email;
        this.password=password;
        this.username = username;
        this.profileId=profileId;
    }



    public void update(String email, String password, String name , UUID profileId) {

        this.email=email;
        this.password=password;
        this.username =name;
        this.profileId=profileId;
        setUpdatedAt();
    }


}

