package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthLoginDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;

public interface AuthService {
    UserResponseDto login(AuthLoginDto dto);
}
