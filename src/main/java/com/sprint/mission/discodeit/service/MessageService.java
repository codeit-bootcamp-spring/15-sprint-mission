package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;

public interface MessageService {

  Message create(MessageCreateRequest request, List<BinaryContentCreateRequest> attachments);

  List<Message> findAllByChannelId(UUID channelId);

  Message update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID id);
}
