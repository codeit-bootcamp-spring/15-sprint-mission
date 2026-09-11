package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    User create(User user);

    User create(String username, String email, String password) throws IOException;

    Optional<User> findById(UUID id);

    List<User> findAll();

    Optional<User> update(UUID id, String name);

    User create(String 길춘배);

    List<User> findall();

    void delete(UUID id) throws IOException;

}

