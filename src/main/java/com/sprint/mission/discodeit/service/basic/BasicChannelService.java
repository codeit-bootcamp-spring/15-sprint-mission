package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelParticipantAddException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelParticipantUpdateException;
import com.sprint.mission.discodeit.exception.channel.PublicChannelRequiredException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;

  public ChannelDto createPublic(PublicChannelCreateRequest request) {
    log.info("Public Channel 생성 요청 : type = PUBLIC, name = {}, desc = {}", request.name(),
        request.description());
    Channel channel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, request.name(), request.description()));
    log.info("Public Channel 생성 완료 : channelId = {}, name = {}", channel.getId(),
        channel.getName());
    return channelMapper.toDto(channel);
  }

  public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
    log.info("PRIVATE Channel 생성 요청 : type = PRIVATE, user = {}", request.participantIds());
    Set<UUID> participants = new HashSet<>(request.participantIds());
    List<User> users = new ArrayList<>();

    for (UUID id : participants) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new UserNotFoundException(id));
      users.add(user);
    }
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    for (User user : users) {
      readStatusRepository.save(new ReadStatus(user, channel, Instant.now()));
    }
    log.info("PRIVATE Channel 생성 완료 : channelId = {}, participantIds = {}", channel.getId(),
        users.stream().map(User::getId).toList());
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto addUserToChannel(UUID channelId, UUID userId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
    if (!ChannelType.PUBLIC.equals(channel.getType())) {
      throw new PrivateChannelParticipantAddException(userId, channelId);
    }
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );
    if (readStatusRepository.findByUserIdAndChannelId(userId, channelId).isEmpty()) {
      readStatusRepository.save(new ReadStatus(user, channel, Instant.now()));
    }
    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  public ChannelDto find(UUID channelId) {
    return channelMapper.toDto(channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId)));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllPublic(UUID channelId) {
    List<ChannelDto> result = new ArrayList<>();
    if (channelId == null) {
      List<Channel> channels =
          channelRepository.findAllByType(ChannelType.PUBLIC);
      return toChannelDto(channels);
    }
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));

    if (ChannelType.PUBLIC.equals(channel.getType())) {
      result.add(channelMapper.toDto(channel));
    }
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    log.debug("채널 목록 조회 요청 : userId = {}", userId);
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
    log.info("채널 정보 수정 요청 : channelId = {}, 수정정보(이름) = {}, 수정정보(설명) = {}", channelId,
        request.name(), request.description());
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
    channel.update(request.name(), request.description());
    log.info("채널 정보 수정 완료 : channelId = {}, 수정정보(이름) = {}, 수정정보(설명) = {}", channel.getId(),
        channel.getName(), channel.getDescription());
    return channelMapper.toDto(channelRepository.save(channel));
  }

  public void delete(UUID id) {
    log.info("채널 삭제 요청 = channelId = {}", id);
    for (Message message : messageRepository.findAllByChannelId(id)) {
      List<BinaryContent> files = new ArrayList<>(message.getAttachments());
      messageRepository.deleteById(message.getId());
      binaryContentRepository.deleteAll(files);
    }
    for (ReadStatus read : readStatusRepository.findAllByChannelId(id)) {
      readStatusRepository.deleteById(read.getId());
    }
    channelRepository.deleteById(id);
    log.info("채널 삭제 완료 = channelId : {}", id);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UUID> getUserIdsInPublicChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
    if (!ChannelType.PUBLIC.equals(channel.getType())) {
      throw new PublicChannelRequiredException(channelId);
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
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
    if (!ChannelType.PUBLIC.equals(channel.getType())) {
      throw new PrivateChannelParticipantUpdateException(channelId);
    }
    ReadStatus readStatuses = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new ReadStatusNotFoundException(userId, channelId));
    readStatusRepository.deleteById(readStatuses.getId());
  }

}
