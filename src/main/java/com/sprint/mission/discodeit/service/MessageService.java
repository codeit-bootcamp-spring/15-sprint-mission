package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.MessageCursorResponse;
import com.sprint.mission.discodeit.dto.Response.MessageDto;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
    MessageCursorResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable);
    MessageDto update(UUID id,MessageUpdateRequest messageUpdateRequest);
    void delete(UUID id);
}
