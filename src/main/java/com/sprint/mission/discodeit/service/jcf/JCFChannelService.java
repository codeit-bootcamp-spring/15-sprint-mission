package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.time.Instant;
import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data = new HashMap<>();
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    public JCFChannelService(MessageRepository messageRepository, ReadStatusRepository readStatusRepository) {
        this.messageRepository = messageRepository;
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public ChannelResponseDto createPublicChannel(PublicChannelCreateDto dto) {
        Channel channel = new Channel(dto.name(), dto.description(), ChannelType.PUBLIC);
        data.put(channel.getId(), channel);
        return ChannelResponseDto.of(channel, null, Collections.emptyList());
    }

    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelCreateDto dto) {
        if (dto.userIds() == null || dto.userIds().isEmpty()) {
            throw new IllegalArgumentException("참여 유저 목록이 비어있습니다.");
        }

        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        data.put(channel.getId(), channel);

        for (UUID userId : dto.userIds()) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId(), Instant.now());
            readStatusRepository.save(readStatus);
        }

        return ChannelResponseDto.of(channel, null, dto.userIds());
    }

    @Override
    public ChannelResponseDto find(UUID id) {
        Channel channel = data.get(id);
        if (channel == null) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다: " + id);
        }
        return toDto(channel);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatus> userReadStatuses = readStatusRepository.findAllByUserId(userId);
        Set<UUID> accessiblePrivateChannelIds = new HashSet<>();
        for (ReadStatus status : userReadStatuses) {
            accessiblePrivateChannelIds.add(status.getChannelId());
        }

        return data.values().stream()
                .filter(c -> c.getType() == ChannelType.PUBLIC || accessiblePrivateChannelIds.contains(c.getId()))
                .map(this::toDto)
                .toList();
    }

    @Override
    public ChannelResponseDto update(ChannelUpdateDto dto) {
        Channel channel = data.get(dto.id());
        if (channel == null) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다: " + dto.id());
        }

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(dto.name(), dto.description());
        return toDto(channel);
    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다: " + id);
        }

        // 연관된 Message 및 ReadStatus 함께 삭제
        List<Message> messages = messageRepository.findAllByChannelId(id);
        for (Message message : messages) {
            messageRepository.deleteById(message.getId());
        }

        List<ReadStatus> statuses = readStatusRepository.findAllByChannelId(id);
        for (ReadStatus status : statuses) {
            readStatusRepository.deleteById(status.getId());
        }

        data.remove(id);
    }

    private ChannelResponseDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(Message::getCreatedAt)
                .filter(Objects::nonNull)
                .max(Instant::compareTo)
                .orElse(null);

        List<UUID> participantUserIds = Collections.emptyList();
        if (channel.getType() == ChannelType.PRIVATE) {
            participantUserIds = readStatusRepository.findAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        return ChannelResponseDto.of(channel, lastMessageAt, participantUserIds);
    }
}