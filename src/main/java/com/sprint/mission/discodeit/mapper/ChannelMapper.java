package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = messageRepository
        .findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
        .map(Message::getCreatedAt).orElse(null);
    List<UserDto> participants = readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(r -> userMapper.toDto(r.getUser())).toList();
    return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(),
        participants, lastMessageAt);
  }
}