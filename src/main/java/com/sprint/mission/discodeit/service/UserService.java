package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest cr, BinaryContentCreateRequest br);
    UserReadRequest find(UUID userId);
    UserReadRequest findByName(String name);
    List<UserReadRequest> findAll();
    void update(UUID userId,UserUpdateRequest updateRequest, BinaryContentUpdateRequest bcur); // 수정할 객체 id , 수정할 내용
    void delete(UUID id);
}
