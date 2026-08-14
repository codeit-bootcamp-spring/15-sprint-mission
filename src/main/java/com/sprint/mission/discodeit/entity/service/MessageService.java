package com.sprint.mission.discodeit.entity.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(String content);
    Message getById(UUID id);
    List<Message> readAll();
    Message update(UUID id, String content);
    boolean deleteByID(UUID id);

}
