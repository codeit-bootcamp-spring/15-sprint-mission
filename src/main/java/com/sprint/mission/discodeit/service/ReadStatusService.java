package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.Request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest);
    //ReadStatus find(UUID id);
    List<ReadStatusDto> findAllByUserId(UUID userId);
    //List<ReadStatus> findAllByChannelId(UUID channelId);
    ReadStatusDto update(UUID id, ReadStatusUpdateRequest readStatusUpdateRequest);
    //List<ReadStatus> updateAllByUserId(UUID userId);
    //List<ReadStatus> updateAllByChannelId(UUID channelId);
    //void delete(UUID id);

}
