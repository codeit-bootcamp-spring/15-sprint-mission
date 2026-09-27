package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;

public interface ReadStatusService {

  ReadStatus create(ReadStatusCreateRequest request);

  ReadStatus find(UUID id);

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  ReadStatus update(UUID statusId, ReadStatusUpdateRequest request);

  void delete(UUID id);
}
