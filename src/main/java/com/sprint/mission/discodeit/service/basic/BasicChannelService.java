package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channels;
  private final UserRepository users;
  private final ReadStatusRepository reads;
  private final MessageRepository messages;
  private final BinaryContentRepository binaries;

  public ChannelResponse createPublic(PublicChannelCreateRequest request) {
    return toResponse(channels.save(new Channel("PUBLIC", request.name(), request.description())));
  }

  public ChannelResponse createPrivate(PrivateChannelCreateRequest request) {
    List<UUID> ids = new ArrayList<>();
    for (UUID id : request.userIds()) {
      users.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
      if (!ids.contains(id)) {
        ids.add(id);
      }
    }
    Channel channel = new Channel("PRIVATE", null, null);
    for (UUID id : ids) {
      channel.addUser(id);
    }
    channels.save(channel);
    for (UUID id : ids) {
      reads.save(new ReadStatus(id, channel.getId(), Instant.now()));
    }
    return toResponse(channel);
  }

  @Override
  public ChannelResponse addUserToChannel(UUID channelId, UUID userId) {
    Channel channel = channels.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    if (!"PUBLIC".equals(channel.getType())) {
      throw new IllegalArgumentException("PRIVATE 채널은 생성 후 참여자를 추가할 수 없습니다.");
    }
    if (!users.existsById(userId)) {
      throw new NoSuchElementException("존재하지 않는 User");
    }
    channel.addUser(userId);
    Channel channel1 = channels.save(channel);
    if (reads.findByUserIdAndChannelId(userId, channelId).isEmpty()) {
      reads.save(new ReadStatus(userId, channelId, Instant.now()));
    }
    return toResponse(channel1);
  }

  public ChannelResponse find(UUID id) {
    return toResponse(channels.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel")));
  }

  @Override
  public List<ChannelResponse> findAllPublic(UUID id) {
    List<ChannelResponse> result = new ArrayList<>();
    if (id == null) {
      for (Channel channel : channels.findAll()) {
        if ("PUBLIC".equals(channel.getType())) {
          result.add(toResponse(channel));
        }
      }
    } else {
      Channel channel = channels.findById(id)
          .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
      if ("PUBLIC".equals(channel.getType())) {
        result.add(toResponse(channel));
      }
    }
    return result;
  }

  public List<ChannelResponse> findAllByUserId(UUID userId) {
    List<ChannelResponse> result = new ArrayList<>();
    for (Channel channel : channels.findAll()) {
      boolean isPublic = "PUBLIC".equals(channel.getType());
      boolean isParticipant = channel.getUserIds().contains(userId);
      if (isPublic || isParticipant) {
        result.add(toResponse(channel));
      }
    }
    return result;
  }

  public ChannelResponse update(UUID channelId, ChannelUpdateRequest request) {
    Channel channel = channels.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    channel.update(request.name(), request.description());
    return toResponse(channels.save(channel));
  }

  public void delete(UUID id) {
    for (Message message : messages.findAllByChannelId(id)) {
      for (UUID attachmentId : message.getAttachmentIds()) {
        binaries.deleteById(attachmentId);
      }
      messages.deleteById(message.getId());
    }
    for (ReadStatus read : reads.findAllByChannelId(id)) {
      reads.deleteById(read.getId());
    }
    channels.deleteById(id);
  }

  @Override
  public List<UUID> getUserIdsInPublicChannel(UUID channelId) {
    Channel channel = channels.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    if (!"PUBLIC".equals(channel.getType())) {
      throw new IllegalArgumentException("PUBLIC 채널만 조회할 수 있습니다.");
    }
    return channel.getUserIds();
  }

  @Override
  public void removeUserFromPublicChannel(UUID channelId, UUID userId) {
    Channel channel = channels.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    if (!"PUBLIC".equals(channel.getType())) {
      throw new IllegalArgumentException("PRIVATE 채널의 참여자는 변경할 수 없습니다.");
    }
    if (!channel.getUserIds().contains(userId)) {
      throw new NoSuchElementException("참여하지 않은 사용자입니다.");
    }
    channel.removeUser(userId);
    channels.save(channel);
    Optional<ReadStatus> read = reads.findByUserIdAndChannelId(userId, channelId);
    if (read.isPresent()) {
      reads.deleteById(read.get().getId());
    }
  }

  private ChannelResponse toResponse(Channel channel) {
    Instant last = null;
    for (Message message : messages.findAllByChannelId(channel.getId())) {
      if (last == null || message.getCreatedAt().isAfter(last)) {
        last = message.getCreatedAt();
      }
    }
    List<UUID> participants = channel.getUserIds();
    return new ChannelResponse(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(), channel.getCreatedAt(), channel.getUpdatedAt(), last,
        participants);
  }


}
