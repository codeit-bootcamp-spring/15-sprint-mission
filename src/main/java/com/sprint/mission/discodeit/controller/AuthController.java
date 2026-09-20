package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.response.LoginResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/login")
public class AuthController {
    private final BasicAuthService authService;

    public AuthController(BasicAuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<LoginResponse>> createLogin(
            @Valid @RequestBody LoginRequest loginRequest
            ) {
        User user = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(LoginResponse.from(user)));
    }

}
