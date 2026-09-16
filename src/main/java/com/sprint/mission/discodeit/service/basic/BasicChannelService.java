package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelResponse createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.channelName(), request.description());
        channelRepository.save(channel);
        return toResponse(channel);
    }

    @Override
    public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(channel);

        for (UUID participantId : request.participantIds()) {
            ReadStatus readStatus = new ReadStatus(participantId, channel.getId(), Instant.now());
            readStatusRepository.save(readStatus);
        }

        return toResponse(channel);
    }

    @Override
    public ChannelResponse read(UUID channelId) {
        Channel channel = channelRepository.read(channelId);
        if (channel == null) {
            throw new NoSuchElementException("존재하지 않는 채널");
        }
        return toResponse(channel);
    }

    @Override
    public List<ChannelResponse> readAllByUserId(UUID userId) {
        return channelRepository.readAllByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ChannelResponse update(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = channelRepository.read(channelId);
        if (channel == null) {
            throw new NoSuchElementException("존재하지 않는 채널");
        }

        if (channel.getChannelType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("수정할 수 없는 채널");
        }

        channel.update(request.channelName(), request.description());
        channelRepository.save(channel);
        return toResponse(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.read(channelId);
        if (channel == null) {
            throw new NoSuchElementException("존재하지 않는 채널");
        }

        messageRepository.readAllByChannelId(channelId)
                .forEach(message -> messageRepository.delete(message.getId()));

        readStatusRepository.deleteAllByChannelId(channelId);

        channelRepository.delete(channelId);
    }

    private ChannelResponse toResponse(Channel channel) {
        Instant lastMessageAt = messageRepository.readAllByChannelId(channel.getId()).stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);

        List<UUID> participantIds = null;
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            participantIds = readStatusRepository.readAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        return new ChannelResponse(
                channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getChannelType().name(),
                lastMessageAt,
                participantIds,
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}