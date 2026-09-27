package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;

public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest request);

  UserStatus find(UUID id);

  List<UserStatus> findAll();

  UserStatus update(UserStatusUpdateRequest request);

  UserStatus updateByUserId(UUID userId);

  void delete(UUID id);
}
