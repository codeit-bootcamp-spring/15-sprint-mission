package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UUID userId);

  UserStatus find(UUID userStatusId);

  List<UserStatus> findAll();

  void update(UUID userStatusId); // 수정 묶음으로 매개변수

  void updateByUserId(UUID userId); // 수정 묶음인데 id를 userid로 바꾼 묶음으로 받아볼까

  UserStatus updateByUserId(UUID userId, Instant newLastActiveAt);

  void delete(UUID userStatusId);
}
