package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String contents, UUID userId, UUID channelId);
    Message read(UUID messageId);
    List<Message> readAll();
    Message update(UUID messageId, String contents);
    void delete(UUID messageId);
}
