package com.sprint.mission.discodeit.entity.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String name, String phoneNum); // 생성할 객체 정보를 받아, 등록 완료된 객체를 반환한다.
    User getById(UUID id); // 특정 대상을 찾기 위해, id를 받아 해당 데이터를 반환한다.
    List<User> readAll(); // 조건없이 저장된 모든 목록을 불러온다.
    User update(UUID id, String name, String phoneNum); // id와 수정할 데이터 정보를 받아 수정완료된 객체를 반환한다.
    boolean deleteById(UUID id); // 삭제할 대상을 지정하기 위해, ID를 받는다.
}
