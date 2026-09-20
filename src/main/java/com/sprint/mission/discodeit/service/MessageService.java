package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest messageCreateRequest);
    Message find(UUID id);
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> findAll();
    Message update(UUID id,MessageUpdateRequest messageUpdateRequest);
    void delete(UUID id);
}
