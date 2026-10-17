package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  @Transactional
  public ChannelResponseDto createPublicChannel(PublicChannelCreateDto dto) {
    Channel channel = new Channel(dto.name(), dto.description(), ChannelType.PUBLIC);
    Channel saved = channelRepository.save(channel);
    return ChannelResponseDto.of(saved, null, Collections.emptyList());
  }

  @Override
  @Transactional
  public ChannelResponseDto createPrivateChannel(PrivateChannelCreateDto dto) {
    if (dto.userIds() == null || dto.userIds().isEmpty()) {
      throw new IllegalArgumentException("참여 유저 목록이 비어있습니다.");
    }

    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    Channel savedChannel = channelRepository.save(channel);

    // 참여 유저들을 조회하여 엔티티 연관관계를 맺고 ReadStatus 생성
    for (UUID userId : dto.userIds()) {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다: " + userId));
      ReadStatus readStatus = new ReadStatus(user, savedChannel, Instant.now());
      readStatusRepository.save(readStatus);
    }

    return ChannelResponseDto.of(savedChannel, null, dto.userIds());
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
      accessiblePrivateChannelIds.add(status.getChannel().getId());
    }

    // PUBLIC 전체 + 본인이 참여한 PRIVATE 채널만 필터링
    return channels.stream()
        .filter(c -> c.getType() == ChannelType.PUBLIC || accessiblePrivateChannelIds.contains(
            c.getId()))
        .map(this::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ChannelResponseDto update(UUID id, ChannelUpdateDto dto) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다: " + id));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    // 변경 감지(Dirty Checking): 엔티티 메서드 호출만으로 트랜잭션 종료 시 UPDATE 반영 (save 불필요)
    channel.update(dto.name(), dto.description());

    return toDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다: " + id));

    // 연관된 Message 및 ReadStatus 삭제
    List<Message> messages = messageRepository.findAllByChannelId(id);
    messageRepository.deleteAll(messages);

    List<ReadStatus> statuses = readStatusRepository.findAllByChannelId(id);
    readStatusRepository.deleteAll(statuses);

    channelRepository.delete(channel);
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
          .map(status -> status.getUser().getId())
          .toList();
    }

    return ChannelResponseDto.of(channel, lastMessageAt, participantUserIds);
  }
}