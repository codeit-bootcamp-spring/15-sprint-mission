package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelResponseDto createPublicChannel(PublicChannelCreateDto dto) {
        Channel channel = new Channel(dto.name(), dto.description(), ChannelType.PUBLIC);
        Channel saved = channelRepository.save(channel);
        return ChannelResponseDto.of(saved, null, Collections.emptyList());
    }

    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelCreateDto dto) {
        if (dto.userIds() == null || dto.userIds().isEmpty()) {
            throw new IllegalArgumentException("참여 유저 목록이 비어있습니다.");
        }

        // PRIVATE 채널은 name과 description 생략
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        Channel saved = channelRepository.save(channel);

        // 채널에 참여하는 User별 ReadStatus 생성
        for (UUID userId : dto.userIds()) {
            ReadStatus readStatus = new ReadStatus(userId, saved.getId(), Instant.now());
            readStatusRepository.save(readStatus);
        }

        return ChannelResponseDto.of(saved, null, dto.userIds());
    }

    @Override
    public ChannelResponseDto find(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다: " + id));

        return toDto(channel);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<ReadStatus> userReadStatuses = readStatusRepository.findAllByUserId(userId);

        Set<UUID> accessiblePrivateChannelIds = new HashSet<>();
        for (ReadStatus status : userReadStatuses) {
            accessiblePrivateChannelIds.add(status.getChannelId());
        }

        // PUBLIC 전체 + 본인이 참여한 PRIVATE 채널만 필터링
        return channels.stream()
                .filter(c -> c.getType() == ChannelType.PUBLIC || accessiblePrivateChannelIds.contains(c.getId()))
                .map(this::toDto)
                .toList();
    }

    @Override
    public ChannelResponseDto update(ChannelUpdateDto dto) {
        Channel channel = channelRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다: " + dto.id()));

        // PRIVATE 채널은 수정 불가
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(dto.name(), dto.description());
        Channel saved = channelRepository.save(channel);
        return toDto(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
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

        channelRepository.deleteById(id);
    }

    private ChannelResponseDto toDto(Channel channel) {
        // 가장 최근 메시지의 시간 정보 조회
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(Message::getCreatedAt)
                .filter(Objects::nonNull)
                .max(Instant::compareTo)
                .orElse(null);

        // PRIVATE 채널인 경우 참여한 User ID 목록 조회
        List<UUID> participantUserIds = Collections.emptyList();
        if (channel.getType() == ChannelType.PRIVATE) {
            participantUserIds = readStatusRepository.findAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        return ChannelResponseDto.of(channel, lastMessageAt, participantUserIds);
    }
}