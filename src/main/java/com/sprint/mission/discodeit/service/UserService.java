package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String userName, String email, String password);
    User read(UUID userId);
    List<User> readAll();
    User update(UUID userId, String userName, String email, String password);
    void delete(UUID userId);
}
