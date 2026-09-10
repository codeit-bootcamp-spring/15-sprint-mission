package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelReadResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    //Channel create(String name, ChannelType channelType);

    Channel create(PublicChannelCreateRequest publicChannelCreateRequest);
    Channel create(PrivateChannelCreateRequest privateChannelCreateRequest);

    ChannelReadResponse read(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, String name, ChannelType channelType);
    void delete(UUID id);


}
