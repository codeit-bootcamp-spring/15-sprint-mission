package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(String channelName);

    void updateChannelName(UUID id,String channelName);

    Channel getChannelInfo(UUID id);

    List<Channel> getAllChannel();

    void deleteChannel(UUID id);

    void addUserToChannel(UUID channelId, UUID userId);

    List<UUID> getUserInChannel(UUID id);

    void deleteUserInChannel(UUID channelId, UUID userId);

}
