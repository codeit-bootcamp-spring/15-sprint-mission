package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message create();

    Message read(UUID id);

    List<Message> readAll();

    Message update(UUID id);

    Message create(UUID channelId, String content);

    void delete(UUID id);
}