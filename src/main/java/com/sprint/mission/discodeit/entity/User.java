package com.sprint.mission.discodeit.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

//계정,비번,닉,등급?
public class User extends BaseClass {
    private String email;
    private String password;
    private String name;
    private NitroLevel nitroLevel;

    @JsonCreator
    public User(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Long createdAt,
            @JsonProperty("updatedAt") Long updatedAt,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("nitroLevel") NitroLevel nitroLevel

    ) {
        super(id, createdAt, updatedAt);
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
    }
    public User(String email, String password, String name, NitroLevel nitroLevel) {
        super();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
    }



    public void update(String email, String password, String name, NitroLevel nitroLevel) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
        setUpdatedAt();
    }



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }


    public NitroLevel getNitroLevel() {
        return nitroLevel;
    }


}

