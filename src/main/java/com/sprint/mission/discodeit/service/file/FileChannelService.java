package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String channelName) {

        Channel channel = new Channel(channelName);

        channelRepository.save(channel);

        return channel;
    }

    @Override
    public Channel read(UUID id) {

        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAll() {

        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String channelName) {

        Channel channel = channelRepository.findById(id);

        if (channel == null) {
            return null;
        }

        channel.setChannelName(channelName);

        channelRepository.save(channel);

        return channel;
    }

    @Override
    public void delete(UUID id) {

        channelRepository.deleteById(id);
    }
}