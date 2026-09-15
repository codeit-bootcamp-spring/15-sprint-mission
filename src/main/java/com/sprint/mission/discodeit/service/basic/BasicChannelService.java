package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final FileChannelRepository channelRepository;

    @Override
    public Channel create(Channel channel) {
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String name, ChannelType type) {
        Optional<Channel> optionalChannel =
                channelRepository.findById(id);

        if (optionalChannel.isEmpty()) {
            return null;
        }

        Channel channel = optionalChannel.get();
        channel.update(name, type);

        channelRepository.save(channel);

        return channel;
    }

    @Override
    public Channel findByid(UUID id) {
        return channelRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}

