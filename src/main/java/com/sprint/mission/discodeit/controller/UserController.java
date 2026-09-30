package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "사용자 등록(회원가입)", description = "새로운 사용자를 등록합니다.")
  @PostMapping
  public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
    UserResponseDto response = userService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "사용자 단건 조회", description = "사용자 ID로 사용자 상세 정보를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> find(@PathVariable("id") UUID id) {
    UserResponseDto response = userService.find(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "전체 사용자 목록 조회", description = "등록된 모든 사용자 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> findAll() {
    List<UserResponseDto> response = userService.findAll();
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 부분 수정합니다.")
  @PatchMapping("/{id}")
  public ResponseEntity<UserResponseDto> update(
      @PathVariable("id") UUID id,
      @RequestBody UserUpdateDto dto) {
    UserResponseDto response = userService.update(id, dto);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자 삭제", description = "사용자 ID로 사용자를 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}