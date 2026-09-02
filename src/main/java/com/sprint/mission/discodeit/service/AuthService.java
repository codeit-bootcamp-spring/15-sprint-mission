package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthRequest;

import java.util.UUID;

public interface AuthService {
    UUID login(AuthRequest authRequest);
}
