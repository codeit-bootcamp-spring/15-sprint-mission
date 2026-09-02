package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String name, String description) {
        Channel channel = new Channel(name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String name, String description) {
        Optional<Channel> optionalChannel = channelRepository.findById(id);
        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.update(name, description);
            return channelRepository.save(channel);
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        if (channelRepository.existsById(id)) {
            channelRepository.deleteById(id);
            return true;
        }
        return false;
    }
}