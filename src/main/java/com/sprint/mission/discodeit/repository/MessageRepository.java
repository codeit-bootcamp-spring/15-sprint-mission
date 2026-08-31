package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageRepository {
    boolean create(Message message);
    List<Message> readAll();
    boolean update(Message message);
    boolean delete(Message message);
}
