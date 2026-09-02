package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message createMessage(Message message);

    Optional<Message> getMessage(UUID id);

    List<Message> getMessageAll();

    void deleteMessage(UUID id);

    boolean existsById(UUID id);
}
