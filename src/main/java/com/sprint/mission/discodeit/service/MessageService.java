package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {


    Message create(
            MessageCreateRequest request,
            List<BinaryContentCreateRequest> attachments
    );

    Message read(UUID id);

    List<Message> findAllByChannelId(UUID channelId);

    Message update(
            UUID id,
            MessageUpdateRequest request
    );

    void delete(UUID id);
}