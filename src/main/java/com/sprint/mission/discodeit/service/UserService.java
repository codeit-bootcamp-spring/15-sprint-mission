package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest cr, BinaryContentCreateRequest br) throws AccountException, IllegalStateException;
    UserResponse find(UUID userId) throws InstanceNotFoundException;
    UserResponse findByName(String name) throws InstanceNotFoundException;
    List<UserResponse> findAll();
    void update(UserUpdateRequest updateRequest, BinaryContentUpdateRequest bcur) throws AccountException, InstanceNotFoundException; // 수정할 객체 id , 수정할 내용
    void delete(UUID id) throws InstanceNotFoundException;
}
