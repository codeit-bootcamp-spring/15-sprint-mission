package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;


    @Override
    public User login(LoginRequest request) {
        // username + password 일치하는 User 찾기
        // 있으면 return
        // 없으면 예외
        User user = userRepository.findByName(request.username())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}