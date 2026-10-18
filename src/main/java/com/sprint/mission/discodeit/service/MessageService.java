package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.MessageDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
    Slice<MessageDto> findAllByChannelId(UUID channelId, int page);
    MessageDto update(UUID id,MessageUpdateRequest messageUpdateRequest);
    void delete(UUID id);
}
