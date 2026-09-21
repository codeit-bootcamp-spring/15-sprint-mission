package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService  {

    ChannelDto createPublic(PublicChannelCreateRequest request);

    ChannelDto createPrivate(PrivateChannelCreateRequest request);

    ChannelDto find(UUID id);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto update(ChannelUpdateRequest request);
    void delete(UUID id);
}
