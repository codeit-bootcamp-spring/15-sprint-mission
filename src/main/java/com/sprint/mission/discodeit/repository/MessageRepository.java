package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    Message load(UUID id);
    boolean delete(UUID id);
}
