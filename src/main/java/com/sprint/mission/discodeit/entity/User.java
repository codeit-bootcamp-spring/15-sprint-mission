package com.sprint.mission.discodeit.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

//계정,비번,닉,등급?

@Getter
public class User extends BaseClass {
    private String email;
    private String password;
    private String username;
    private NitroLevel nitroLevel;
    private UUID profileId;

    @JsonCreator
    public User(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("username") String username,
            @JsonProperty("nitroLevel") NitroLevel nitroLevel,
            @JsonProperty("profileId") UUID profileId

    ) {
        super(id, createdAt, updatedAt);
        this.email=email;
        this.password=password;
        this.username = username;
        this.nitroLevel=nitroLevel;
        this.profileId=profileId;
    }
    public User(String email, String password, String username, NitroLevel nitroLevel, UUID profileId) {
        super();
        this.email=email;
        this.password=password;
        this.username = username;
        this.nitroLevel=nitroLevel;
        this.profileId=profileId;
    }



    public void update(String email, String password, String name, NitroLevel nitroLevel, UUID profileId) {

        this.email=email;
        this.password=password;
        this.username =name;
        this.nitroLevel=nitroLevel;
        this.profileId=profileId;
        setUpdatedAt();
    }






}

