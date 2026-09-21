package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Set;
import java.util.UUID;

public interface UserRepository {

  boolean create(User user);

  User find(UUID userId);

  User findByName(String name);

  Set<User> findAll();

  boolean update(User user);

  void delete(UUID userId);
}
