package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  // 1. 단독 User 변환 (online은 null 처리하여 7개 인자 맞춤)
  public UserDto toDto(User user) {
    if (user == null) {
      return null;
    }

    BinaryContentDto profileDto = binaryContentMapper.toDto(user.getProfile());

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        profileDto,
        null // UserDto의 7번째 필드인 Boolean online에 맞춤
    );
  }

  // 2. 온라인 상태를 알고 있을 때 사용하는 toDto 오버로딩
  public UserDto toDto(User user, boolean online) {
    if (user == null) {
      return null;
    }

    BinaryContentDto profileDto = binaryContentMapper.toDto(user.getProfile());

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        profileDto,
        online
    );
  }

  // 3. UserResponseDto 반환용 메서드
  public UserResponseDto toResponseDto(User user, boolean online) {
    if (user == null) {
      return null;
    }

    BinaryContentDto profileDto = binaryContentMapper.toDto(user.getProfile());

    return new UserResponseDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        profileDto,
        online
    );
  }
}