package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    User create(User user);

    User create(String username, String email, String password) throws IOException;

    Optional<User> findById(UUID id);

    List<User> findAll();

    Optional<Object> update(UUID id, String name);

    void delete(UUID id) throws IOException;

    List<User> findall();
}

