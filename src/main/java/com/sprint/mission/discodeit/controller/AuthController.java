package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.AuthLoginDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인 (POST /api/auth/login)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> login(@RequestBody AuthLoginDto dto) {
        UserResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}