package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class UserCreateRequest {

    private String username;
    private String email;
    private String password;
    private UUID profileImageId;

    public UserCreateRequest(String username, String email, String password, UUID profileImageId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageId = profileImageId;
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

    public UUID getProfileImageId() {
        return profileImageId;
    }

}

