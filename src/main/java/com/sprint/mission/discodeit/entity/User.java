package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.util.UUID;

@Getter
public class User extends Common {
    private String userName;
    private String email;
    private String password;
    private UUID profileId;

    public User(String userName, String email, String password, UUID profileId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update(String userName, String email, String password, UUID profileId) {
        if (userName != null) {
            this.userName = userName;
        }

        if (email != null) {
            this.email = email;
        }

        if (password != null) {
            this.password = password;
        }

        if (profileId != null) {
            this.profileId = profileId;
        }

        updateUpdatedAt();
    }
}
