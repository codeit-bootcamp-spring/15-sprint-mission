package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.ReadStatus;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  protected MessageRepository messageRepository;
  @Autowired
  protected ReadStatusRepository readStatusRepository;
  @Autowired
  protected UserMapper userMapper;

  @Mapping(target = "participants", expression = "java(findParticipants(channel))")
  @Mapping(target = "lastMessageAt", expression = "java(findLastMessageAt(channel))")
  public abstract ChannelDto toDto(Channel channel);

  protected List<UserDto> findParticipants(Channel channel) {
    return readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(readStatus -> userMapper.toDto(readStatus.getUser()))
        .toList();
  }

  protected Instant findLastMessageAt(Channel channel) {
    return messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
        .map(Message::getCreatedAt)
        .orElse(null);
  }
}