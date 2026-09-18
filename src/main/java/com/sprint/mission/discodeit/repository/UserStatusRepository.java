package com.sprint.mission.discodeit.repository;

import ch.qos.logback.core.status.Status;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    Optional<UserStatus> findByUserId(UUID userId);
    List<UserStatus> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);            // 상태 PK 기준 존재 확인
    boolean existsByUserId(UUID userId);    // 유저 ID 기준 존재 확인
}
