package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "사용자 등록(회원가입)", description = "새로운 사용자를 등록합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponseDto> create(
      @RequestPart("userCreateRequest") UserCreateDto dto,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException {

    BinaryContentCreateDto profileDto = null;
    if (profile != null && !profile.isEmpty()) {
      profileDto = new BinaryContentCreateDto(
          profile.getOriginalFilename(),
          profile.getBytes(),
          profile.getContentType() != null ? profile.getContentType() : "application/octet-stream"
      );
    }

    UserCreateDto finalDto = new UserCreateDto(
        dto.username(),
        dto.email(),
        dto.password(),
        profileDto
    );

    UserResponseDto response = userService.create(finalDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // ⬇️ 누락되었던 GET 단건 조회
  @Operation(summary = "사용자 단건 조회", description = "사용자 ID로 사용자 상세 정보를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> find(@PathVariable("id") UUID id) {
    UserResponseDto response = userService.find(id);
    return ResponseEntity.ok(response);
  }

  // ⬇️ 누락되었던 GET 전체 목록 조회
  @Operation(summary = "전체 사용자 목록 조회", description = "등록된 모든 사용자 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> findAll() {
    List<UserResponseDto> response = userService.findAll();
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 부분 수정합니다.")
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponseDto> update(
      @PathVariable("id") UUID id,
      @RequestPart("userUpdateRequest") UserUpdateDto dto,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException {

    BinaryContentCreateDto newProfileImage = null;
    if (profile != null && !profile.isEmpty()) {
      newProfileImage = new BinaryContentCreateDto(
          profile.getOriginalFilename(),
          profile.getBytes(),
          profile.getContentType() != null ? profile.getContentType() : "application/octet-stream"
      );
    }

    UserUpdateDto finalDto = new UserUpdateDto(
        dto.username(),
        dto.email(),
        dto.password(),
        newProfileImage
    );

    UserResponseDto response = userService.update(id, finalDto);
    return ResponseEntity.ok(response);
  }

  // ⬇️ 사용자 삭제
  @Operation(summary = "사용자 삭제", description = "사용자 ID로 사용자를 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "사용자 상태 갱신/조회", description = "사용자의 마지막 활동 시간을 갱신하고 상태를 조회합니다.")
  @RequestMapping(value = "/{userId}/userStatus", method = {RequestMethod.GET, RequestMethod.PATCH,
      RequestMethod.PUT})
  public ResponseEntity<?> updateUserStatus(@PathVariable("userId") UUID userId) {
    // 1. 현재 시간으로 접속 시간 갱신
    UserStatus status = userStatusService.update(userId, java.time.Instant.now());

    // 2. 프록시 객체 순환 참조 방지를 위해 DTO 또는 Map으로 변환하여 반환
    Map<String, Object> response = new HashMap<>();
    response.put("id", status.getId());
    response.put("userId", userId);
    response.put("lastActiveAt", status.getLastActiveAt());

    return ResponseEntity.ok(response);
  }
}