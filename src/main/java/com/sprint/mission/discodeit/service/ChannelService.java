package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {



    ChannelDto create(ChannelCreateRequest request);

    Channel create(Channel channel);

    Channel create(ChannelType type, String name, String description);

    Optional<ChannelDto> findById(UUID id);

    List<ChannelDto> findAll();

    Optional<ChannelDto> update(ChannelUpdateRequest request);

    Channel update(UUID id, String name, ChannelType type);

    Channel findByid(UUID id);

    void delete(UUID id);

}

