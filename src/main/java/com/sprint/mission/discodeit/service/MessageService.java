package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(UUID channelId, UUID authorId, String content);

    List<Message> getMessagesByChannel(UUID channelId);

    List<Message> getMessagesByUser(UUID userId);

    void updateMessageContents(UUID uuid,String newContents);

    void deleteMessage(UUID id);
}
