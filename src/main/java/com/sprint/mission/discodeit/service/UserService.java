package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest);
    List<UserDto> findAll();
    UserDto update(UUID id, UserUpdateRequest userUpdateRequest,Optional<BinaryContentCreateRequest> binaryContentCreateRequest);
    void delete(UUID id);

}
