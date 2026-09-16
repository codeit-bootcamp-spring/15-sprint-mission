package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelDto createPublic(PublicChannelCreateRequest request) {

        Channel channel = new Channel(
                request.name(),
                ChannelType.PUBLIC
        );

        Channel savedChannel = channelRepository.save(channel);

        return new ChannelDto(
                savedChannel.getId(),
                savedChannel.getName(),
                savedChannel.getType(),
                List.of(),
                null,
                savedChannel.getCreatedAt(),
                savedChannel.getUpdatedAt()
        );
    }

    @Override
    public ChannelDto find(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 채널입니다. ID: " + id )
                );
        List<UUID> participantIds = List.of();

        if (channel.getType() == ChannelType.PRIVATE) {
            participantIds = readStatusRepository
                    .findAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId) .toList();
        }
        java.time.Instant lastMessageAt = messageRepository
                .findAllByChannelId(channel.getId()) .stream()
                .map(message ->
                        message.getCreatedAt())
                .max(java.time.Instant::compareTo)
                .orElse(null);

        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getType(),
                participantIds,
                lastMessageAt,
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {

        userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(
                        "존재하지 않는 사용자입니다. ID: " + userId
                )
        );

        return channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType() == ChannelType.PUBLIC
                || readStatusRepository
                                .findByUserIdAndChannelId(userId, channel.getId())
                                .isPresent()
                )
                .map(channel -> {

                    List<UUID> participantIds = List.of();

                    if (channel.getType() == ChannelType.PRIVATE){
                        participantIds = readStatusRepository
                                .findAllByChannelId(channel.getId())
                                .stream()
                                .map(ReadStatus::getUserId)
                                .toList();
                    }

                    java.time.Instant lastMessageAt = messageRepository
                            .findAllByChannelId(channel.getId())
                            .stream()
                            .map(message -> message.getCreatedAt())
                            .max(java.time.Instant::compareTo)
                            .orElse(null);

                    return new ChannelDto(
                            channel.getId(),
                            channel.getName(),
                            channel.getType(),
                            participantIds,
                            lastMessageAt,
                            channel.getCreatedAt(),
                            channel.getUpdatedAt()
                    );
                })
                .toList();
    }

    @Override
    public ChannelDto update(ChannelUpdateRequest request) {

        Channel channel = channelRepository.findById(request.id())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 채널입니다. ID: " + request.id()
                        )
                );

        if (channel.getType() == ChannelType.PRIVATE) {
            throw  new IllegalArgumentException(
                    "PRIVATE 채널은 수정할 수 없습니다."
            );
        }

        channel.update(request.name());

        Channel savedChannel = channelRepository.save(channel);

        java.time.Instant lastMessageAt = messageRepository
                .findAllByChannelId(savedChannel.getId())
                .stream()
                .map(message -> message.getCreatedAt())
                .max(java.time.Instant::compareTo)
                .orElse(null);

        return new ChannelDto(
                savedChannel.getId(),
                savedChannel.getName(),
                savedChannel.getType(),
                List.of(),
                lastMessageAt,
                savedChannel.getCreatedAt(),
                savedChannel.getUpdatedAt()
        );
    }

    @Override
    public void delete(UUID id) {

        channelRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 채널입니다. ID: " + id
                        )
                );

        messageRepository.findAllByChannelId(id)
                .forEach(message ->
                        messageRepository.deleteById(message.getId())
                );

        readStatusRepository.findAllByChannelId(id)
                .forEach(readStatus ->
                        readStatusRepository.deleteById(readStatus.getId())
                );
        channelRepository.deleteById(id);
    }

    @Override
    public ChannelDto createPrivate(PrivateChannelCreateRequest request){

        request.participantIds().forEach(userId ->
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "존재하지 않는 사용자입니다. ID: " + userId
                                )
                        )
        );
        Channel channel = new Channel(
                null,
                ChannelType.PRIVATE
        );

        Channel savedChannel = channelRepository.save(channel);

        request.participantIds().forEach(userId -> {
            ReadStatus readStatus = new ReadStatus(
                    userId,
                    savedChannel.getId(),
                    java.time.Instant.now()
            );
            readStatusRepository.save(readStatus);
        });

        return new ChannelDto(
                savedChannel.getId(),
                savedChannel.getName(),
                savedChannel.getType(),
                request.participantIds(),
                null,
                savedChannel.getCreatedAt(),
                savedChannel.getUpdatedAt()
        );
    }
}
