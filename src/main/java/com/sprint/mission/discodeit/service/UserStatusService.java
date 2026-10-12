package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  // 인터페이스에 선언되어 있던 create 메서드
  UserStatus create(UserStatusCreateRequest request);

  // 접속 시간 갱신
  UserStatus update(UUID userId, Instant lastActiveAt);

  // 유저 ID 기준 조회
  UserStatus findByUserId(UUID userId);
}
