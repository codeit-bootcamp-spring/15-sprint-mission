package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.LoginRequest;
import com.sprint.mission.discodeit.dto.Response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
        User user=authService.login(loginRequest);
        UserDto userDto = userService.toUserResponse(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDto);
    }


}
