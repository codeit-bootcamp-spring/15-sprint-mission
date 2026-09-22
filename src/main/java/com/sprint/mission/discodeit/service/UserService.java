package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest);
    UserResponse find(UUID id);
    List<UserResponse> findAll();
    User update(UUID id, UserUpdateRequest userUpdateRequest);
    void delete(UUID id);
    UserResponse toUserResponse(User user);
}
