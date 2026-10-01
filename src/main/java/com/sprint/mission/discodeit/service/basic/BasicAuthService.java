package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService  {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDto login(LoginRequest request) {

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자입니다: " + request.username()
                        )
                );

        if (!user.getPassword().equals(request.password())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "사용자 상태가 존재하지 않습니다. User ID: " + user.getId()
                        )
                );

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isOnline(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
