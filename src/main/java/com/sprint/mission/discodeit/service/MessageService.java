package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(UUID userId, UUID channelID, String Message);
    Message read(UUID id);
    List<Message> readAll();
    Message update(UUID id, String Message);
    Message delete(UUID id);

}
