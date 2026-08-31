package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Set;

public interface UserRepository {
    boolean create(User user);
    Set<User> readAll();
    boolean update(User user);
    boolean delete(User user);
}
