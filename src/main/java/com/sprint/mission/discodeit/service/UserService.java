package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserReadResponse;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest);
    UserReadResponse find(UUID id);
    List<UserReadResponse> findAll();
    User update(UUID id, UserUpdateRequest userUpdateRequest);
    void delete(UUID id);

}
