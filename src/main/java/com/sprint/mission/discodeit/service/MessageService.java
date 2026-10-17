package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
    //Message find(UUID id);
    Slice<MessageDto> findAllByChannelId(UUID channelId, int page);
    //List<Message> findAll();
    MessageDto update(UUID id,MessageUpdateRequest messageUpdateRequest);
    void delete(UUID id);
}
