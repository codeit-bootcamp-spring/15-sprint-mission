package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.LoginRequest;
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
    public User login(LoginRequest loginRequest) {
        User user;
        user=userRepository.findByName(loginRequest.name()).orElseThrow(() -> new NoSuchElementException("로그인 오류. name이 일치하는 user 없음 : "+ loginRequest.name()));
        if(!user.getPassword().equals(loginRequest.password())){
            throw new IllegalArgumentException("비밀번호가 다름");
        }
        return user;
    }
}
