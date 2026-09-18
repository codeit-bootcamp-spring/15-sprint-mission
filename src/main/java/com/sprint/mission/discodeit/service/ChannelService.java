package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto createPublicChannel(PublicChannelCreateDto dto);
    ChannelResponseDto createPrivateChannel(PrivateChannelCreateDto dto);
    ChannelResponseDto find(UUID id);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    ChannelResponseDto update(ChannelUpdateDto dto);
    void delete(UUID id);
}