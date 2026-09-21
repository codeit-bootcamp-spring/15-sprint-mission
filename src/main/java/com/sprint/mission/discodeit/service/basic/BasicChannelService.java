package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.exception.ConflictException;
import com.sprint.mission.discodeit.dto.exception.NotFoundException;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  @Override
  public Channel createPublicChannel(ChannelCreateRequest cr) {
    Channel channel = new Channel(ChannelType.PUBLIC, cr.name(), cr.description());
    List<Channel> channels = channelRepository.findAll();

    // 중복 여부 확인
    for (Channel c : channels) {
      if (c.getType().equals(ChannelType.PUBLIC) && c.getName().equals(cr.name())) {
        throw new ConflictException("채널 이름은 중복될 수 없습니다.");
      }
    }

    boolean isCreated = channelRepository.create(channel);
    if (!isCreated) {
      throw new IllegalStateException("채널 저장 중 오류가 발생했습니다.");
    }

    return channel;

  }

  @Override
  public Channel createPrivateChannel(List<UUID> userIds) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);

    boolean isCreated = channelRepository.create(channel);
    if (!isCreated) {
      throw new IllegalStateException("채널 저장 중 오류가 발생했습니다.");
    }

    for (UUID u : userIds) {
      ReadStatus readStatus = new ReadStatus(u, channel.getId());
      readStatusRepository.save(readStatus);
    }
    return channel;

  }

  @Override
  public ChannelResponse find(UUID channelId) {
    Channel channel = channelRepository.find(channelId);
    if (channel == null) {
      throw new NotFoundException("채널을 찾지 못했습니다");
    }

    ChannelResponse channelResponse;
    Instant latestTime = messageRepository.findByChannelId(channelId).stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())
        .orElse(null);

    if (channel.getType().equals(ChannelType.PRIVATE)) { // Private 채널일 경우
      // readStatus 에서 프라이빗 채널에 속한 UserID를 가져오는 과정이 필요함.
      List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(channelId);
      if (readStatus == null) {
        throw new NotFoundException("읽을 파일이 없습니다.");
      }

      List<UUID> users = readStatus.stream().map(ReadStatus::getUserId).toList(); //UserId를 배열로

      channelResponse = new ChannelResponse(
          channel.getId(),
          channel.getType(),
          channel.getName(),
          channel.getDescription(),
          latestTime,
          users);
    } else {
      channelResponse = new ChannelResponse(
          channel.getId(),
          channel.getType(),
          channel.getName(),
          channel.getDescription(),
          latestTime,
          null); // new ArrayList<>() 도 고려해볼만 함.
    }

    return channelResponse;
  }

  @Override
  public ChannelResponse findByChannelName(String channelName) {
    Channel channel = channelRepository.findByChannelName(channelName);
    if (channel == null) {
      throw new NotFoundException("채널을 찾지 못했습니다");
    }
    UUID channelId = channel.getId();

    ChannelResponse channelResponse;
    Instant latestTime = messageRepository.findByChannelId(channelId).stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())
        .orElse(null);

    if (channel.getType().equals(ChannelType.PRIVATE)) { // Private 채널일 경우
      // readStatus 에서 프라이빗 채널에 속한 UserID를 가져오는 과정이 필요함.
      List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(channelId);
      if (readStatus.isEmpty()) {
        throw new NotFoundException("읽을 파일이 없습니다.");
      }

      List<UUID> users = readStatus.stream().map(ReadStatus::getUserId).toList(); //UserId를 배열로

      channelResponse = new ChannelResponse(
          channel.getId(),
          channel.getType(),
          channel.getName(),
          channel.getDescription(),
          latestTime,
          users);
    } else {
      channelResponse = new ChannelResponse(
          channel.getId(),
          channel.getType(),
          channel.getName(),
          channel.getDescription(),
          latestTime,
          null);
    }

    return channelResponse;
  }

  @Override
  public List<ChannelResponse> findAllByUserId(UUID userId) {
    List<ChannelResponse> result = new ArrayList<>();
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
    List<Channel> channels = channelRepository.findAll();

    for (Channel c : channels) {
      Instant latestTime = messageRepository.findByChannelId(c.getId()).stream()
          .map(Message::getCreatedAt)
          .max(Comparator.naturalOrder())
          .orElse(null);

      if (c.getType().equals(ChannelType.PRIVATE)) {
        if (readStatuses.isEmpty()) {
          continue;
        }

        for (ReadStatus r : readStatuses) {
          if (c.getId().equals(r.getChannelId())) {
            List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(c.getId());
            if (readStatus.isEmpty()) {
              throw new NotFoundException("읽을 파일이 없습니다.");
            }

            List<UUID> users = readStatus.stream().map(ReadStatus::getUserId).toList();
            result.add(new ChannelResponse(c.getId(), c.getType(), c.getName(), c.getDescription(), latestTime, users));
          }
        }
      } else {
        result.add(new ChannelResponse(c.getId(), c.getType(), c.getName(), c.getDescription(), latestTime, null));
      }
    }

    return result;
  }

  @Override
  public Channel update(ChannelUpdateRequest cu) {
    Channel channel = channelRepository.find(cu.id());
    if (channel == null) {
      throw new NotFoundException("채널을 찾지 못했습니다");
    }
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalStateException("Private 채널은 수정이 불가능합니다.");
    }

    channel.setName(cu.name());
    channel.setDescription(cu.description());
    channel.autoSetUpdatedAt();

    channelRepository.update(channel);
    System.out.println("정상적으로 메시지가 업데이트 되었습니다.");

    return channel;
  }

  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.find(channelId);
    if (channel == null) {
      throw new NotFoundException("존재하지 않는 채널입니다.");
    }

    List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(channelId);
    List<Message> message = messageRepository.findByChannelId(channelId);

    readStatus.forEach(r -> readStatusRepository.delete(r.getId()));
    message.forEach(m -> messageRepository.delete(m.getId()));
    channelRepository.delete(channelId);
  }
}
