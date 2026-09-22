package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.LoginRequest;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.ApiResponse;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest loginRequest) {
        User user=authService.login(loginRequest);
        UserResponse userResponse = userService.toUserResponse(user);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }


}
