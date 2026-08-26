package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User createUser(User user);

    Optional<User> getUser(UUID id);

    List<User> getUserAll();

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
