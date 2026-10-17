package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final BinaryContentStorage binaryContentStorage;

  public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");

  @Tag(name = "유저 생성")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestParam(value = "profile", required = false) MultipartFile multipartFile)
      throws IOException {
    BinaryContentCreateRequest profile = null;
    if (multipartFile != null && !multipartFile.isEmpty()) {
      validateImageFile(multipartFile);
      byte[] photo = multipartFile.getBytes();
      String fileName = multipartFile.getOriginalFilename();
      String extension = multipartFile.getContentType();
      profile = new BinaryContentCreateRequest(fileName, extension, photo);
    }
    UserDto user01 = userService.create(userCreateRequest, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(user01);
  }

  @Tag(name = "유저 생성 프로필 첨부파일 검증")
  private void validateImageFile(MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new IllegalArgumentException("파일이 비었습니다.");
    }
    String fileName = multipartFile.getOriginalFilename();
    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new IllegalArgumentException("허용되지 않은 확장자 입니다.");
    }
    String contentType = multipartFile.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
    }
  }

  @Tag(name = "유저 호출")
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUser(@PathVariable("userId") UUID userId) {
    UserDto userResponse = userService.find(userId);
    return ResponseEntity.ok().body(userResponse);
  }

  @Tag(name = "전체 유저 호출")
  @GetMapping
  public ResponseEntity<List<UserDto>> allUser() {
    List<UserDto> allUser = userService.findAll();
    return ResponseEntity.ok().body(allUser);
  }

  @Tag(name = "유저 프로필 호출")
  @GetMapping("/{userId}/image")
  public ResponseEntity<?> userImage(@PathVariable("userId") UUID userId) {
    BinaryContentDto profile = userService.find(userId).profile();
    if (profile == null) {
      throw new NoSuchElementException("프로필 이미지가 없습니다.");
    }
    return binaryContentStorage.download(profile);
  }

  @Tag(name = "유저 정보 수정")
  @PatchMapping("/{userId}")
  public ResponseEntity<UserDto> updateUser(@PathVariable("userId") UUID userId,
      @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestParam(required = false) MultipartFile profile) throws IOException {
    BinaryContentCreateRequest profile1 = null;
    if (profile != null && !profile.isEmpty()) {
      validateImageFile(profile);
      byte[] photo = profile.getBytes();
      String fileName = profile.getOriginalFilename();
      String extension = profile.getContentType();
      profile1 = new BinaryContentCreateRequest(fileName, extension, photo);
    }
    UserDto user = userService.update(userId, userUpdateRequest, profile1);
    return ResponseEntity.ok().body(user);
  }

  @Tag(name = "유저 삭제")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  // 심화요구사항 DTO 변환
  @Tag(name = "전체 유저 호출")
  @GetMapping("/findAll")
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok().body(userService.findAll());
  }

  @Tag(name = "특정 User의 온라인 상태 수정")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> update(@PathVariable("userId") UUID userId) {
    UserStatusDto status = userStatusService.updateByUserId(userId);
    return ResponseEntity.ok().body(status);
  }

}
