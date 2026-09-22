package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {


    User create(String username, String email, String password) throws IOException;

    UserDto create(UserCreateRequest request);

    Optional<UserDto> findById(UUID id);

    List<UserDto> findAll();

    Optional<UserDto> update(UserUpdateRequest request);

    void delete(UUID id);

    void updateStatus(UUID userId);
}

