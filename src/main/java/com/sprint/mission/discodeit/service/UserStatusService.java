package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import java.util.*;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest request);

  UserStatusDto find(UUID id);

  List<UserStatusDto> findAll();

  UserStatusDto update(UserStatusUpdateRequest request);

  UserStatusDto updateByUserId(UUID userId);

  void delete(UUID id);
}
