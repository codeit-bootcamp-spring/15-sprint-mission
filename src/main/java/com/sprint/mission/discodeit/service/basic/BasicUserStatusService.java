package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    // 요구사항: 순환 참조 방지를 위해 Service 대신 Repository 직접 주입
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        // 1. User 미존재 검증
        if (!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("사용자가 존재하지 않습니다: " + request.userId());
        }

        // 2. 1인 1상태 제약 조건 검증 (이미 등록된 유저 상태인지 체크)
        if (userStatusRepository.existsByUserId(request.userId())) {
            throw new IllegalArgumentException("해당 사용자의 상태가 이미 존재합니다.");
        }

        UserStatus userStatus = new UserStatus(
                request.userId(),
                request.lastActiveAt()
        );

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserStatus를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("수정할 UserStatus가 존재하지 않습니다: " + request.id()));

        // 엔티티의 update 메서드 호출 (lastActiveAt 갱신 및 touch)
        userStatus.update(request.lastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new NoSuchElementException("삭제할 UserStatus가 존재하지 않습니다: " + id);
        }
        userStatusRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userStatusRepository.existsByUserId(userId);
    }
}