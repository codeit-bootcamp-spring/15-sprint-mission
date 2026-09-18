package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // [심화 요구사항] 사용자 목록 조회 (GET /api/user/findAll)
    @GetMapping("/api/user/findAll")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> response = userService.findAll().stream()
                .map(user -> new UserDto(
                        user.id(),
                        user.createdAt(),
                        user.updatedAt(),
                        user.username(),
                        user.email(),
                        user.profileImageId(),
                        user.isOnline()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    // 1. 사용자 등록 (POST /api/users)
    @PostMapping("/api/users")
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
        UserResponseDto response = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 단건 조회 (GET /api/users/{id})
    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponseDto> find(@PathVariable("id") UUID id) {
        UserResponseDto response = userService.find(id);
        return ResponseEntity.ok(response);
    }

    // 3. 모든 사용자 조회 (GET /api/users) - 경로 누락 수정
    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    // 4. 사용자 정보 수정 (PATCH /api/users/{id})
    @PatchMapping("/api/users/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable("id") UUID id,
            @RequestBody UserUpdateDto dto) {
        UserResponseDto response = userService.update(dto);
        return ResponseEntity.ok(response);
    }

    // 5. 사용자 삭제 (DELETE /api/users/{id})
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}