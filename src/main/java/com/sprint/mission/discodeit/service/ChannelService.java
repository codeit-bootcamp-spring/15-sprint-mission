package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    //Channel create(String name, ChannelType channelType);

    Channel create(PublicChannelCreateRequest publicChannelCreateRequest);
    Channel create(PrivateChannelCreateRequest privateChannelCreateRequest);

    ChannelFindResponse find(UUID id);
    List<Channel> findAll();
    List<Channel> findAllByUserId(UUID userId);
    Channel update(ChannelUpdateRequest channelUpdateRequest);
    void delete(UUID id);


}
