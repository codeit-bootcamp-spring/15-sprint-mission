package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String email, String password, String name, NitroLevel nitroLevel);
    User read(UUID id);
    List<User> readAll();
    User update(UUID id, String email, String password, String name, NitroLevel nitroLevel);
    void delete(UUID id);

}
