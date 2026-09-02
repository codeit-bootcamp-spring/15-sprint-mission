package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public UUID login(AuthRequest authRequest) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(authRequest.username()) && u.getPassword().equals(authRequest.password())) {
                    return u.getId();
            }
        }

        return null;
    }
}
