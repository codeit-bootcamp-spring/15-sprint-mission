package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserFindResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest);
    UserFindResponse find(UUID id);
    List<UserFindResponse> findAll();
    User update(UUID id, UserUpdateRequest userUpdateRequest);
    void delete(UUID id);

}
