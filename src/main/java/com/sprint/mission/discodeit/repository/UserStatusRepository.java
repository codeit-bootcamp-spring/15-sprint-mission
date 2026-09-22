package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserStatusRepository {



    //저장
    UserStatus save(UserStatus userStatus);

    // 상태 ID로 찾기
    Optional<UserStatus> findById(UUID id);

    // findAll() 전체 상태 조회
    List<UserStatus> findAll();

    // findByUserId() 특정 사용자의 상태 조회
    Optional<UserStatus> findByUserId(UUID userId);

    // 삭제
    void delete(UUID id);



}
