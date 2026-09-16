package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    boolean save(UserStatus userStatus);
    UserStatus find(UUID id);
    UserStatus findByUserId(UUID userid);
    List<UserStatus> findAll();
    boolean delete(UUID id);
}
