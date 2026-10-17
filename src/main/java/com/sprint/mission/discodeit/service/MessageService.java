package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachments);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

  MessageDto update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID id);
}
