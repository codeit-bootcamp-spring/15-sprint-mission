package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import java.util.*;

public interface UserService {

  UserDto create(UserCreateRequest request, BinaryContentCreateRequest profile);

  UserDto find(UUID id);

  List<UserDto> findAll();

  UserDto update(UUID userId, UserUpdateRequest request, BinaryContentCreateRequest profile);

  void delete(UUID id);
}
