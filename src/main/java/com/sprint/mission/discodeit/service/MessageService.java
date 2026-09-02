package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds);
    List<Message> userReadAll(User user);
    List<Message> channelReadAll(Channel channel);
    Message read(UUID channelId, UUID userId);
    List<Message> readAll();
    void update(UUID id, String content);
    void delete(Message message);
}
