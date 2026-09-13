package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);
    UserStatus read(UUID id);
    List<UserStatus> readAll();
    UserStatus readByUserId(UUID userId);
    void delete(UUID id);
    void deleteByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}
