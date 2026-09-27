package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository users;

  private final UserStatusRepository statuses;

  public UserResponse login(LoginRequest request) {
    Optional<User> result = users.findByUsername(request.username());
    if (result.isEmpty() || !result.get().getPassword().equals(request.password())) {
      throw new IllegalArgumentException("로그인 정보가 일치하지 않습니다.");
    }
    User user = result.get();
    UserStatus userStatus = statuses.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException("접속 상태가 없습니다."));
    userStatus.update();
    statuses.save(userStatus);
    return toResponse(user);
  }

  private UserResponse toResponse(User user) {
    boolean online = false;
    Optional<UserStatus> status = statuses.findByUserId(user.getId());
    if (status.isPresent()) {
      online = status.get().isOnline();
    }
    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
        user.getCreatedAt(), user.getUpdatedAt(), user.getProfileId(), online);
  }
}
