package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserFindResponse;
import com.sprint.mission.discodeit.dto.UserDto.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;


    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST)
    public UserFindResponse create(
            @RequestBody UserCreateApiRequest request
    ) {

        User user = userService.create(
                request.user(),
                request.profile()
        );

        return userService.find(user.getId());
    }


    // 모든 사용자 조회
    @RequestMapping(
            path = "/findAll",
            method = RequestMethod.GET
    )
    public List<UserFindResponse> findAll() {

        return userService.findAll();
    }


    // 사용자 수정
    @RequestMapping(
            path = "/{userId}",
            method = RequestMethod.PATCH
    )
    public UserFindResponse update(
            @PathVariable("userId") UUID userId,
            @RequestBody UserUpdateApiRequest request
    ) {

        User user = userService.update(
                userId,
                request.user(),
                request.profile()
        );

        if (user == null) {
            return null;
        }

        return userService.find(user.getId());
    }


    // 사용자 삭제
    @RequestMapping(
            path = "/{userId}",
            method = RequestMethod.DELETE
    )
    public void delete(
            @PathVariable("userId") UUID userId
    ) {

        userService.delete(userId);
    }


    // 사용자의 온라인 상태 업데이트
    @RequestMapping(
            path = "/{userId}/status",
            method = RequestMethod.PATCH
    )
    public UserStatus updateStatus(
            @PathVariable("userId") UUID userId,
            @RequestBody UserStatusUpdateRequest request
    ) {

        return userStatusService.updateByUserId(
                userId,
                request
        );
    }


    // 사용자 생성 요청 묶음
    public record UserCreateApiRequest(
            UserCreateRequest user,
            BinaryContentCreateRequest profile
    ) {
    }


    // 사용자 수정 요청 묶음
    public record UserUpdateApiRequest(
            UserUpdateRequest user,
            BinaryContentCreateRequest profile
    ) {
    }
}