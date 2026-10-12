package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      return null;
    }

    // 최신 메시지 작성 시각 확인
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
        .map(Message::getCreatedAt)
        .filter(Objects::nonNull)
        .max(Instant::compareTo)
        .orElse(null);

    // PRIVATE 채널인 경우 참여자 ID 리스트 추출
    List<UUID> participantUserIds = Collections.emptyList();
    if (channel.getType() == ChannelType.PRIVATE) {
      participantUserIds = readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(readStatus -> readStatus.getUser().getId())
          .toList();
    }

    return new ChannelDto(
        channel.getId(),
        channel.getCreatedAt(),
        channel.getUpdatedAt(),
        channel.getName(),
        channel.getDescription(),
        channel.getType(),
        lastMessageAt,
        participantUserIds
    );
  }
}