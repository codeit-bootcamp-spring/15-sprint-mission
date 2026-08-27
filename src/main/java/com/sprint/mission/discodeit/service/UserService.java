package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.NitroLevel;

import java.util.UUID;

public interface UserService {
    void create(String email, String password, String name, NitroLevel nitroLevel);
    void read();
    void update(UUID id, String email, String password, String name, NitroLevel nitroLevel);
    void delete(UUID id);

}
