package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;




    @Override
    public ChannelDto create(ChannelCreateRequest request) {

        Channel channel = new Channel(
                request.getType(),
                request.getName(),
                request.getDescription()
        );

        channelRepository.save(channel);

        return new ChannelDto(channel.getId(),channel.getName());
    }

    @Override
    public Optional<ChannelDto> findById(UUID id) {
        return channelRepository.findById(id).map(channel ->  new ChannelDto(
                channel.getId(), channel.getName()
        ));
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll().stream().map(channel -> new ChannelDto(
                channel.getId(), channel.getName()
        )).toList();
    }

    @Override
    public Optional<ChannelDto> update(ChannelUpdateRequest request) {
        return channelRepository.findById(request.getId()).map(channel -> {

            channel.update(request.getName(),request.getType());

            channelRepository.save(channel);

            return new ChannelDto(
                    channel.getId(), channel.getName()
            );
        });
    }



    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);

    }

}

