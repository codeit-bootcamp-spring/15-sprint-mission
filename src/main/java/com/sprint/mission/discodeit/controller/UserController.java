package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private static final List<String> ALLOWED_EXTENTIONS = List.of("jpg", "jpeg", "png");
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final AuthService authService;

    // 유저 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> createUser(
            @RequestPart("userCreateRequest") UserCreateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

        Optional<BinaryContentCreateRequest> profileRequest = (profile == null || profile.isEmpty())
                ? Optional.empty()
                : Optional.of(new BinaryContentCreateRequest(
                profile.getOriginalFilename(),
                profile.getContentType(),
                profile.getBytes()
        ));

        User user = userService.create(request, profileRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }

    // 유저 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @Valid @RequestBody UserUpdateRequest request, @PathVariable String userId) {
        User user = userService.update(UUID.fromString(userId), request, Optional.empty());
        return ResponseEntity.status((HttpStatus.ACCEPTED))
                .body(UserResponse.from(user));
    }

    // 유저 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("userId") UUID userId){
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    // 유저 상태 변경
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResponse> updateUserStatus(
            @Valid @RequestBody UserStatusUpdateRequest request,
            @PathVariable("userId") UUID userId) {

        UserStatus userStatus = userStatusService.updateByUserId(userId, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(UserStatusResponse.from(userStatus));
    }

    // 전체 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
