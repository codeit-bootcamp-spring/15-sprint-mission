package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    Message create(String content, UUID authorId, UUID channelId);
    Optional<Message> read(UUID id);
    List<Message> readAll();
    List<Message> readAllByChannelId(UUID channelId);
    Message update(UUID id, String content);
    boolean delete(UUID id);
}
