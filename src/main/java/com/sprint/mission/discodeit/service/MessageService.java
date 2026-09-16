package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest mcr, List<BinaryContentCreateRequest> bccr) throws InstanceNotFoundException;
    List<Message> findAllByChannelId(UUID channelId);
    void update(MessageUpdateRequest mur);
    void delete(UUID messageId);
}
