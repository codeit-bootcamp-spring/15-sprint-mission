package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;
public interface UserService {
    UserResponse create(UserCreateRequest request, BinaryContentCreateRequest profile);
    UserResponse find(UUID id);
    List<UserResponse> findAll();
    UserResponse update(UserUpdateRequest request, BinaryContentCreateRequest profile);
    void delete(UUID id);
}
