package com.sprint.mission.discodeit.repository;

import ch.qos.logback.core.status.Status;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  // User 엔티티의 id(UUID)를 기준으로 UserStatus 조회
  Optional<UserStatus> findByUserId(UUID userId);

  // User 엔티티의 id(UUID)를 기준으로 UserStatus 존재 여부 확인
  boolean existsByUserId(UUID userId);   // 유저 ID 기준 존재 확인
}
