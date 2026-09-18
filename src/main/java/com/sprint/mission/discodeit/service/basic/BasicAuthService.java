package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthLoginDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
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
    public UserResponseDto login(AuthLoginDto dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + dto.username()));

        if (!user.getPassword().equals(dto.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시 온라인 상태를 true로 반환하거나, UserResponseDto 변환 규칙 적용
        return UserResponseDto.of(user, true);
    }
}
