package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public interface ChannelService {
    Channel getChannel(UUID id);
    List<Channel> getAllChannels();
    Channel createChannel(String channelName);
    Channel updateChannel(UUID id, String channelName);
    Channel deleteChannel (UUID id);
}
