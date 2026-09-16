package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    boolean create(Message message);
    Message find(UUID id);
    List<Message> findByChannelId(UUID id);
    List<Message> findByAuthorId(UUID id);
    List<Message> findAll();
    boolean update(Message message);
    boolean delete(UUID id);
}
