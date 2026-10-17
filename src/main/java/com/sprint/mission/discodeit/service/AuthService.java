package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

public interface AuthService {

  UserDto login(LoginRequest request);
}
