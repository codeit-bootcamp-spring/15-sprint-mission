package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageCursorResponse;
import com.sprint.mission.discodeit.dto.response.MessageDto;
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
