package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(LoginRequest request) {
        return userRepository.readAll().stream()
                .filter(user -> user.getUserName().equals(request.userName())
                        && user.getPassword().equals(request.password()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("일치하는 유저 없음"));
    }
}