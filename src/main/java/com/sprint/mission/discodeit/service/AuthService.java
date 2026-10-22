package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.LoginRequest;
import com.sprint.mission.discodeit.dto.Response.UserDto;

public interface AuthService {
    UserDto login(LoginRequest loginRequest);
}
