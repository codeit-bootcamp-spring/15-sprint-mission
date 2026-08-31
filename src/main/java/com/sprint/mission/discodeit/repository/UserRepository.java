package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    User load(UUID id);
    List<User> loadValue();
    boolean delete(UUID id);
}
