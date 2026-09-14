package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.UserDto.LoginRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserFindRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    // 로그인
    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST
    )
    public UserFindRequest login(
            @RequestBody LoginRequest request
    ) {

        User user = authService.login(request);

        // 비밀번호를 응답하지 않기 위해 UserFindRequest로 변환
        return userService.find(user.getId());
    }
}