package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.exception.auth.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicAuthService implements AuthService {

  private final UserRepository users;
  private final UserStatusRepository statuses;
  private final UserMapper userMapper;

  public UserDto login(LoginRequest request) {
    log.info("로그인 입력 정보 : userName : {} ", request.username());
    Optional<User> result = users.findByUsername(request.username());
    if (result.isEmpty() || !result.get().getPassword().equals(request.password())) {
      throw new InvalidCredentialsException(request.username());
    }
    User user = result.get();
    log.info("로그인 userName : {}", user.getUsername());
    UserStatus userStatus = statuses.findByUserId(user.getId())
        .orElseThrow(() -> UserStatusNotFoundException.byUserId(user.getId()));
    userStatus.update();
    statuses.save(userStatus);
    log.info("유저 로그인 완료 : userName = {}", user.getUsername());
    return userMapper.toDto(user);
  }

}
