package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class User extends Common {
    private String user;
    private String email;
    private String userId;

    private UUID profileId;
}
