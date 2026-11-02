package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
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
  private final UserMapper userMapper;

  public ChannelDto createPublic(PublicChannelCreateRequest request) {
    return channelMapper.toDto(
        channelRepository.save(
            new Channel(ChannelType.PUBLIC, request.name(), request.description())));
  }

  public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
    Set<UUID> participants = new HashSet<>(request.participantIds());
    List<User> ids = new ArrayList<>();

    for (UUID id : participants) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
      ids.add(user);
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
      List<Channel> channels =
          channelRepository.findAllByType(ChannelType.PUBLIC);
      return toChannelDto(channels);
    }
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));

    if (ChannelType.PUBLIC.equals(channel.getType())) {
      result.add(channelMapper.toDto(channel));
    }
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<Channel> channels = channelRepository.findVisibleChannels(userId, ChannelType.PUBLIC);
    return toChannelDto(channels);
  }

  private List<ChannelDto> toChannelDto(List<Channel> channels) {
    if (channels.isEmpty()) {
      return List.of();
    }
    List<UUID> channelIds = new ArrayList<>();
    Map<UUID, List<UserDto>> participantsByChannelId = new HashMap<>();
    // 채널별로 참여자를 담을 빈 목록 준비
    for (Channel channel : channels) {
      channelIds.add(channel.getId());
      participantsByChannelId.put(channel.getId(), new ArrayList<>());
    }
    // 참여자를 한 번에 조회한 뒤 해당 채널 목록에 넣기
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelIdIn(channelIds);
    for (ReadStatus readStatus : readStatuses) {
      UUID channelId = readStatus.getChannel().getId();
      UserDto participant = userMapper.toDto(readStatus.getUser());
      participantsByChannelId.get(channelId).add(participant);
    }
    // 마지막 메시지 시각도 한 번에 조회
    Map<UUID, Instant> lastMessageAtByChannelId = new HashMap<>();
    List<ChannelLastMessageAt> lastMessages = messageRepository.findLastMessageAtByChannelIds(
        channelIds);
    for (ChannelLastMessageAt lastMessage : lastMessages) {
      lastMessageAtByChannelId.put(
          lastMessage.channelId(),
          lastMessage.lastMessageAt()
      );
    }
    // 준비한 데이터를 DTO로 변환
    List<ChannelDto> result = new ArrayList<>();

    for (Channel channel : channels) {
      result.add(channelMapper.toDto(
          channel,
          participantsByChannelId.get(channel.getId()),
          lastMessageAtByChannelId.get(channel.getId())
      ));
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
