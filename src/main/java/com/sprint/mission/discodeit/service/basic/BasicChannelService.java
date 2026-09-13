package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto.ChannelFindRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPublic(PublicChannelCreateRequest request) {

        Channel channel = new Channel(
                request.ownerId(),
                request.channelName(),
                request.channelDescription(),
                ChannelType.PUBLIC
        );

        channelRepository.save(channel);

        return channel;
    }

    @Override
    public Channel createPrivate(PrivateChannelCreateRequest request) {

        // 1. 참여자 목록 검증
        if (request.userIds() == null || request.userIds().isEmpty()) {
            throw new IllegalArgumentException("참여 사용자가 필요합니다.");
        }

        // 2. 중복 사용자 제거
        List<UUID> userIds = request.userIds().stream()
                .distinct()
                .toList();

        // 3. 실제 존재하는 사용자 검사
        for (UUID userId : userIds) {
            if (userId == null || !userRepository.existsById(userId)) {
                throw new IllegalArgumentException(
                        "존재하지 않는 참여 사용자입니다.");
            }
        }

        // 4. 검증이 끝난 후 채널 생성
        Channel channel = new Channel(
                request.ownerId(),
                null,
                null,
                ChannelType.PRIVATE
        );

        channelRepository.save(channel);

        // 5. 검증된 사용자만 ReadStatus 생성
        for (UUID userId : userIds) {
            ReadStatus readStatus = new ReadStatus(channel.getId(), userId
                    // 기존 생성자에 맞게
            );

            readStatusRepository.save(readStatus);
        }

        return channel;
    }

    @Override
    public ChannelFindRequest find(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("채널이 없습니다."));

        return toDto(channel);
    }

    @Override
    public List<ChannelFindRequest> findAllByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        return channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType() == ChannelType.PUBLIC
                                || readStatusRepository
                                .findAllByChannelId(channel.getId())
                                .stream()
                                .anyMatch(status ->
                                        userId.equals(status.getUserId())))
                .map(this::toDto)
                .toList();
    }

    @Override
    public Channel update(UUID id, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("채널이 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException(
                    "PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(
                request.channelName(),
                request.channelDescription()
        );

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new IllegalArgumentException("채널이 없습니다.");
        }

        messageRepository.deleteByChannelId(id);
        readStatusRepository.deleteByChannelId(id);
        channelRepository.deleteById(id);
    }

    private ChannelFindRequest toDto(Channel channel) {
        Instant lastMessageAt = messageRepository
                .findAllByChannelId(channel.getId())
                .stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);

        List<UUID> userIds =
                channel.getType() == ChannelType.PRIVATE
                        ? readStatusRepository
                        .findAllByChannelId(channel.getId())
                        .stream()
                        .map(ReadStatus::getUserId)
                        .distinct()
                        .toList()
                        : List.of();

        return new ChannelFindRequest(
                channel.getId(),
                channel.getOwnerId(),
                channel.getChannelName(),
                channel.getChannelDescription(),
                lastMessageAt,
                userIds
        );
    }
}