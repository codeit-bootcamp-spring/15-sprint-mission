package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import java.util.*;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest request);

  ReadStatusDto find(UUID id);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  List<ReadStatusDto> findAllByChannelId(UUID channelId);

  ReadStatusDto update(UUID statusId, ReadStatusUpdateRequest request);

  void delete(UUID id);
}
