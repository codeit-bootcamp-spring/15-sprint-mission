package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    //Channel create(String name, ChannelType channelType);

    Channel create(PublicChannelCreateRequest publicChannelCreateRequest);
    Channel create(PrivateChannelCreateRequest privateChannelCreateRequest);

    ChannelResponse find(UUID id);
    List<ChannelResponse> findAll();
    List<ChannelResponse> findAllByUserId(UUID userId);
    Channel update(UUID id,ChannelUpdateRequest channelUpdateRequest);
    void delete(UUID id);
    ChannelResponse toChannelResponse(Channel channel);


}
