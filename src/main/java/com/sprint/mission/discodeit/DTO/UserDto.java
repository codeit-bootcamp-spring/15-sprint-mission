package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public class UserDto {

    private UUID id;
    private String username;

    public UserDto(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


}
