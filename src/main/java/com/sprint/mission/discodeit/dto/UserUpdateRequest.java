package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class UserUpdateRequest {

    private UUID id;
    private String username;
    private UUID profileImageId;

    public UserUpdateRequest(UUID id, String username, UUID profileImageId) {
        this.id = id;
        this.username = username;
        this.profileImageId = profileImageId;

    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UUID getProfileImageId() {
        return profileImageId;
    }
}
