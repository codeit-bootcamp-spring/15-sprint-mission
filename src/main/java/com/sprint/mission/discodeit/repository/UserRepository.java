package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserRepository {
    void save(User user);
    User load(UUID id);
    boolean delete(UUID id);
}
