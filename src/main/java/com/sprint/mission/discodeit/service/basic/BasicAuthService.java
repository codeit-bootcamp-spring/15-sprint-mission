package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthLoginDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponseDto login(AuthLoginDto dto) {
    System.out.println(">>> 로그인 시도 username: " + dto.username());
    System.out.println(">>> 입력받은 password: " + dto.password());

    User user = userRepository.findByUsername(dto.username())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + dto.username()));

    System.out.println(">>> DB에 저장된 password: " + user.getPassword());

    if (!user.getPassword().equals(dto.password())) {
      System.out.println(">>> 비밀번호 불일치!");
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    return userMapper.toResponseDto(user, true);
  }
}
