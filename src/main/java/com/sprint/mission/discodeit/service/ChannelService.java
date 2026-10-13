package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    //Channel create(String name, ChannelType channelType);

    ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest);
    ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest);
    List<ChannelDto> findAllByUserId(UUID userId);
    ChannelDto update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest);
    void delete(UUID id);
    ChannelDto toChannelResponse(Channel channel);


}
