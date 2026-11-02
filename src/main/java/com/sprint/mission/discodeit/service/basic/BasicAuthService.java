package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional(readOnly = true)
    public UserDto login(LoginRequest loginRequest) {
        User user;
        user=userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new NoSuchElementException("로그인 오류. name이 일치하는 user 없음 : "+ loginRequest.username()));
        if(!user.getPassword().equals(loginRequest.password())){
            throw new IllegalArgumentException("비밀번호가 다름");
        }

        UserDto dto = userMapper.toDto(user);
        log.info("로그인 성공: userId={}", user.getId());
        return dto;
    }
}
