package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.AuthRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponse> userLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password)
    {
        AuthRequest authRequest = new AuthRequest(username, password);

        return ResponseEntity.ok().body(userService.find(authService.login(authRequest).getId()));
    }
}
