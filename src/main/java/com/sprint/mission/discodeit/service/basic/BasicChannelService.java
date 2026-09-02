package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicChannelService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        return channelRepository.createChannel(channel);
    }

    @Override
    public void updateChannelName(UUID id, String channelName) {
        Channel channel = findChannel(id);
        channel.update(channelName);
        channelRepository.createChannel(channel);
    }

    @Override
    public Channel getChannelInfo(UUID id) {
        return findChannel(id);
    }

    @Override
    public List<Channel> getAllChannel() {
        return channelRepository.getChannelAll();
    }

    @Override
    public void deleteChannel(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. id = " + id);
        }
        channelRepository.deleteChannel(id);
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = findChannel(channelId);
        userRepository.getUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id = " + userId));
        channel.addUserToChannel(userId);
        channelRepository.createChannel(channel);
    }

    @Override
    public List<UUID> getUserInChannel(UUID id) {
        Channel channel = findChannel(id);
        return channel.getUserInChannel();
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        Channel channel = findChannel(channelId);
        userRepository.getUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id = " + userId));
        channel.deleteUserToChannel(userId);
        channelRepository.createChannel(channel);
    }

    private Channel findChannel(UUID id) {
        return channelRepository.getChannel(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다. id = " + id));
    }
}
