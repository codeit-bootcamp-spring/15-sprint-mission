package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository reads;

  private final UserRepository users;

  private final ChannelRepository channels;

  public ReadStatus create(ReadStatusCreateRequest request) {
    users.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    Channel channel = channels.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));

    // 읽음 상태를 새로 만들더라도 PRIVATE의 참여자 목록은 바꾸지 않습니다.
    if ("PRIVATE".equals(channel.getType())
        && !channel.getUserIds().contains(request.userId())) {
      throw new IllegalArgumentException("PRIVATE 채널의 참여자가 아닙니다.");
    }
    if (reads.findByUserIdAndChannelId(request.userId(), request.channelId()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 읽음 상태입니다.");
    }
    return reads.save(new ReadStatus(request.userId(), request.channelId(), request.lastReadAt()));
  }

  public ReadStatus find(UUID id) {
    return reads.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 ReadStatus: " + id));
  }

  public List<ReadStatus> findAllByUserId(UUID userId) {
    return reads.findAllByUserId(userId);
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return reads.findAllByChannelId(channelId);
  }

  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus status = find(readStatusId);
    status.update(request.lastReadAt());
    return reads.save(status);
  }

  public void delete(UUID id) {
    reads.deleteById(id);
  }
}
