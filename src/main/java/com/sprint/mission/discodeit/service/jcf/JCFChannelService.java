package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;
    private final JCFUserService jcfUserService;

    public JCFChannelService(JCFUserService jcfUserService) {
        this.data = new HashMap<>();
        this.jcfUserService = jcfUserService;
    }


    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        data.put(channel.getId(),channel);
        return channel;
    }

    @Override
    public void updateChannelName(UUID id, String channelName) {
        Channel channel = findChannel(id);
        channel.update(channelName);
    }

    @Override
    public Channel getChannelInfo(UUID id) {
        return findChannel(id);
    }

    @Override
    public List<Channel> getAllChannel() {
        return data.values().stream().toList();
    }

    @Override
    public void deleteChannel(UUID id) {
        if (data.remove(id) == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. id = " + id);
        }
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = findChannel(channelId);
        User user = jcfUserService.getUser(userId);
        channel.addUserToChannel(user);
    }

    @Override
    public List<User> getUserInChannel(UUID id) {
        Channel channel = findChannel(id);
        return channel.getUserInChannel();
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        Channel channel = findChannel(channelId);
        User user = jcfUserService.getUser(userId);
        channel.deleteUserToChannel(user);
    }

    private Channel findChannel(UUID id) {
        Channel channel = data.get(id);
        if (channel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. id = " + id);
        }
        return channel;
    }


}
