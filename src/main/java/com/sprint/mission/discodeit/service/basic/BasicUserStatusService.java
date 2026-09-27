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
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository statuses;
  private final UserRepository users;

  public UserStatus create(UserStatusCreateRequest request) {
    users.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    if (statuses.findByUserId(request.userId()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 접속 상태입니다.");
    }
    return statuses.save(new UserStatus(request.userId(), request.lastActiveAt()));
  }

  public UserStatus find(UUID id) {
    return statuses.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus: " + id));
  }

  public List<UserStatus> findAll() {
    return statuses.findAll();
  }

  public UserStatus update(UserStatusUpdateRequest request) {
    UserStatus status = find(request.id());
    status.update();
    return statuses.save(status);
  }

  public UserStatus updateByUserId(UUID userId) {
    UserStatus status = statuses.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("접속 상태가 없습니다."));
    status.update();
    return statuses.save(status);
  }

  public void delete(UUID id) {
    statuses.deleteById(id);
  }
}
