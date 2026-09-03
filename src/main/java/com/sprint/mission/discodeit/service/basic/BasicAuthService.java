package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;

import javax.security.auth.login.AccountNotFoundException;

@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public User login(AuthRequest authRequest) throws Exception {
            for (User u : userRepository.findAll()) {
                if (u.getUsername().equals(authRequest.username()) && u.getPassword().equals(authRequest.password())) {
                    return u;
                }
            }
            throw new AccountNotFoundException("해당하는 User를 찾을 수 없음");
    }
}
