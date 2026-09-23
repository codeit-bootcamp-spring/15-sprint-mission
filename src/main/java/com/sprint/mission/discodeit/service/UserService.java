package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserFindResponse;
import com.sprint.mission.discodeit.dto.UserDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

    //유저 생성기능
    User create(
            UserCreateRequest userRequest,
            BinaryContentCreateRequest profileRequest
    );

    //유저의 id로 조회하는 기능
    UserFindResponse find(UUID id);

    //등록된 모든 유저를 조회하는 기능
    List<UserFindResponse> findAll();

    //등록된 유저의 정보를 수정하는 기능
    User update( UUID id,
                 UserUpdateRequest userRequest,
                 BinaryContentCreateRequest profileRequest);

    //등록된 유저의 id로 유저의 정보를 삭제하는 기능
    void delete(UUID id);

}