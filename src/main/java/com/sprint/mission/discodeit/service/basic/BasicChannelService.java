package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String channelName) {
        Channel channel = new Channel(channelName);
        return channelRepository.save(channel);
    }

    @Override
    public Channel read(UUID channelId) {
        return channelRepository.read(channelId);
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.readAll();
    }

    @Override
    public Channel update(UUID channelId, String channelName) {
        Channel channel = channelRepository.read(channelId);
        channel.update(channelName);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }
}