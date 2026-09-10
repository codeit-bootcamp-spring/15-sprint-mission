package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelReadResponse;
import com.sprint.mission.discodeit.dto.Response.UserReadResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;



    @Override
    public Channel create(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(publicChannelCreateRequest.name(), ChannelType.PUBLIC);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null,ChannelType.PRIVATE);
        List<UUID> membersId = privateChannelCreateRequest.membersId();
        ReadStatus readStatus;
        for (UUID entry : membersId){
            readStatus= new ReadStatus(entry, channel.getId());
            readStatusRepository.save(readStatus);
        }

        return channelRepository.save(channel);
    }

    @Override
    public ChannelReadResponse read(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + id));

        return toChannelReadResponse(channel);
    }

    public ChannelReadResponse toChannelReadResponse(Channel channel){
        List<UUID> memberIds = readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        Instant latestMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
        return new ChannelReadResponse(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getName(),
                channel.getChannelType(),
                memberIds,
                latestMessageAt

        );
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String name, ChannelType channelType) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + id));
        channel.update(name, channelType);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new NoSuchElementException("채널 id 없음 : " + id);
        }
        channelRepository.deleteById(id);

    }


}
