package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserService userService;

  @Override
  public UserResponse login(LoginRequest request) {
    User user = userRepository.readByUserName(request.username());

    if (user == null || !user.getPassword().equals(request.password())) {
      throw new NoSuchElementException("일치하는 유저 없음");
    }

    return userService.read(user.getId());
  }
}