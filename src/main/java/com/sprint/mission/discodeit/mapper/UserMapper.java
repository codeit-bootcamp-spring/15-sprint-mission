package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.base.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    BinaryContentDto profile = user.getProfile() == null
        ? null : binaryContentMapper.toDto(user.getProfile());
    Boolean online = user.getStatus() == null ? null : user.getStatus().isOnline();
    return new UserDto(user.getId(), user.getUsername(), user.getEmail(), profile, online);
  }
}