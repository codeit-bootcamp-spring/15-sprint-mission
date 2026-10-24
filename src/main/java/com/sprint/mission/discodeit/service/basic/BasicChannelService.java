package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ChannelMapper channelMapper;

  public ChannelDto createPublic(PublicChannelCreateRequest request) {
    return channelMapper.toDto(
        channelRepository.save(
            new Channel(ChannelType.PUBLIC, request.name(), request.description())));
  }

  public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
    Set<User> ids = new HashSet<>();
    for (UUID id : request.participantIds()) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
      if (!ids.contains(id)) {
        ids.add(user);
      }
    }
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);
    for (User id : ids) {
      readStatusRepository.save(new ReadStatus(id, channel, Instant.now()));
    }
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto addUserToChannel(UUID channelId, UUID userId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    if (!ChannelType.PUBLIC.equals(channel.getType())) {
      throw new IllegalArgumentException("PRIVATE 채널은 생성 후 참여자를 추가할 수 없습니다.");
    }
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("존재하지 않는 User");
    }
    User user = userRepository.findById(userId).orElseThrow();
    if (readStatusRepository.findByUserIdAndChannelId(userId, channelId).isEmpty()) {
      readStatusRepository.save(new ReadStatus(user, channel, Instant.now()));
    }
    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  public ChannelDto find(UUID id) {
    return channelMapper.toDto(channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel")));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllPublic(UUID id) {
    List<ChannelDto> result = new ArrayList<>();
    if (id == null) {
      for (Channel channel : channelRepository.findAll()) {
        if (ChannelType.PUBLIC.equals(channel.getType())) {
          result.add(channelMapper.toDto(channel));
        }
      }
    } else {
      Channel channel = channelRepository.findById(id)
          .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
      if (ChannelType.PUBLIC.equals(channel.getType())) {
        result.add(channelMapper.toDto(channel));
      }
    }
    return result;
  }

  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<ChannelDto> result = new ArrayList<>();
    Set<UUID> joinedChannelIds = new HashSet<>();
    for (ReadStatus read : readStatusRepository.findAllByUserId(userId)) {
      joinedChannelIds.add(read.getChannel().getId());
    }
    for (Channel channel : channelRepository.findAll()) {
      boolean isPublic = ChannelType.PUBLIC.equals(channel.getType());
      boolean isParticipant = joinedChannelIds.contains(channel.getId());
      if (isPublic || isParticipant) {
        result.add(channelMapper.toDto(channel));
      }
    }
    return result;
  }

  public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    channel.update(request.name(), request.description());
    return channelMapper.toDto(channelRepository.save(channel));
  }

  public void delete(UUID id) {
    for (Message message : messageRepository.findAllByChannelId(id)) {
      List<BinaryContent> files = new ArrayList<>(message.getAttachments());
      messageRepository.deleteById(message.getId());
      binaryContentRepository.deleteAll(files);
    }
    for (ReadStatus read : readStatusRepository.findAllByChannelId(id)) {
      readStatusRepository.deleteById(read.getId());
    }
    channelRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UUID> getUserIdsInPublicChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    if (!ChannelType.PUBLIC.equals(channel.getType())) {
      throw new IllegalArgumentException("PUBLIC 채널만 조회할 수 있습니다.");
    }
    List<UUID> users = new ArrayList<>();
    for (ReadStatus read : readStatusRepository.findAllByChannelId(channelId)) {
      users.add(read.getUser().getId());
    }
    return users;
  }

  @Override
  public void removeUserFromPublicChannel(UUID channelId, UUID userId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    if (!ChannelType.PUBLIC.equals(channel.getType())) {
      throw new IllegalArgumentException("PRIVATE 채널의 참여자는 변경할 수 없습니다.");
    }
    ReadStatus readStatuses = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new NoSuchElementException("참여하지 않은 사용자입니다."));
    readStatusRepository.deleteById(readStatuses.getId());
  }

}
