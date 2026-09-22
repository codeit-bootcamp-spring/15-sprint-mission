package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MessageService {




    Message create(String content, UUID channelId, UUID authorId);

    Optional<Message> findById(UUID id);

    List<Message> findAll();

    Optional<Message> update(UUID id, String content);

    void delete(UUID id);

    void like(UUID messageId, UUID userId);

    void unlike(UUID messageId, UUID userId);

    Set<UUID> getLikeUserIds(UUID messageId);



}
