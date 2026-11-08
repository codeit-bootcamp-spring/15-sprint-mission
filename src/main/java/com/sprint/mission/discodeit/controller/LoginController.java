package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

  private final AuthService authService;

  @Tag(name = "로그인 기능")
  @PostMapping("/login")
  public ResponseEntity<UserDto> loginUser(
      @Valid @RequestBody LoginRequest request) {
    log.debug("로그인 API 요청 : userName = {}", request.username());
    UserDto user = authService.login(request);
    // 쿠키저장?
    // 추후 기능 추가를 위한 로그인 틀?
    log.debug("로그인 API 요청 완료 : userName = {}", user.username());
    return ResponseEntity.ok().body(user);
  }

}
