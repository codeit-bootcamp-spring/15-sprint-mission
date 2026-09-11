package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus user);
    Optional<UserStatus> findById(UUID id);
    List<UserStatus> findAll();
    Optional<UserStatus> findByUserId(UUID userId);
    void deleteById(UUID id);
    void deleteByUserId(UUID id);
    boolean existsById(UUID id);
    boolean existsByUserId(UUID userId);
}
