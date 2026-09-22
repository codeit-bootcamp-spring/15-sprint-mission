package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.AuthRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<User> userLogin(
      @RequestBody AuthRequest authRequest) {

    return ResponseEntity.ok().body(authService.login(authRequest));
  }
}
