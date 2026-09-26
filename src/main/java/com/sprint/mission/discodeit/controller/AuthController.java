package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.response.LoginResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth/login")
public class AuthController {
    private final BasicAuthService authService;

    public AuthController(BasicAuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "로그인", operationId = "login")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않음")
    })
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<LoginResponse>> createLogin(
            @Valid @RequestBody LoginRequest loginRequest
            ) {
        User user = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(LoginResponse.from(user)));
    }

}
