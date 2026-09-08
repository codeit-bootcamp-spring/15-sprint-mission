package com.sprint.mission.discodeit.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

//계정,비번,닉,등급?

@Getter
public class User extends BaseClass {
    private String email;
    private String password;
    private String name;
    private NitroLevel nitroLevel;
    private UUID profileId;

    @JsonCreator
    public User(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("nitroLevel") NitroLevel nitroLevel,
            @JsonProperty("profileId") UUID profileId

    ) {
        super(id, createdAt, updatedAt);

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
        this.profileId=profileId;
    }
    public User(String email, String password, String name, NitroLevel nitroLevel, UUID profileId) {
        super();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
        this.profileId=profileId;
    }



    public void update(String email, String password, String name, NitroLevel nitroLevel, UUID profileId) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
        this.profileId=profileId;
        setUpdatedAt();
    }






}

