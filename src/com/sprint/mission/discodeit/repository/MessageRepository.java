package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    Message save(Message message);
    Optional<Message> findById(String id);
    List<Message> findAllByChannelId(String channelId);
    List<Message> findAll();
    void deleteById(String id);
    boolean existById(String id);
}
