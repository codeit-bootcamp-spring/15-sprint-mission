package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");

  @Tag(name = "유저 생성")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<UserResponse>> createUser(
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
    UserResponse user01 = userService.create(userCreateRequest, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(user01));
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
  public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable("userId") UUID userId) {
    UserResponse userResponse = userService.find(userId);
    return ResponseEntity.ok().body(ApiResponse.success(userResponse));
  }

  @Tag(name = "전체 유저 호출")
  @GetMapping
  public ResponseEntity<ApiResponse<List<UserResponse>>> allUser() {
    List<UserResponse> allUser = userService.findAll();
    return ResponseEntity.ok().body(ApiResponse.success(allUser));
  }

  @Tag(name = "유저 프로필 호출")
  @GetMapping("/{userId}/image")
  public ResponseEntity<byte[]> userImage(@PathVariable("userId") UUID userId) {
    UUID profileId = userService.find(userId).profileId();
    BinaryContent binaryContent = binaryContentService.find(profileId);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
        .body(binaryContent.getBytes());
  }

  @Tag(name = "유저 정보 수정")
  @PatchMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable("userId") UUID userId,
      @Valid @ModelAttribute UserUpdateRequest userUpdateRequest,
      @RequestParam(required = false) MultipartFile multipartFile) throws IOException {
    BinaryContentCreateRequest profile = null;
    if (multipartFile != null && !multipartFile.isEmpty()) {
      validateImageFile(multipartFile);
      byte[] photo = multipartFile.getBytes();
      String fileName = multipartFile.getOriginalFilename();
      String extension = multipartFile.getContentType();
      profile = new BinaryContentCreateRequest(fileName, extension, photo);
    }
    UserResponse user = userService.update(userUpdateRequest, profile);
    return ResponseEntity.ok().body(ApiResponse.success(user));
  }

  @Tag(name = "유저 삭제")
  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  // 심화요구사항 DTO 변환
  @Tag(name = "전체 유저 호출")
  @GetMapping("/findAll")
  public ResponseEntity<ApiResponse<List<UserDto>>> findAll() {
    List<UserResponse> allUser = userService.findAll();
    List<UserDto> dtoList = new ArrayList<>();
    if (!allUser.isEmpty()) {
      for (UserResponse user : allUser) {
        dtoList.add(new UserDto(user.id(), user.createdAt(), user.updatedAt(),
            user.username(), user.email(), user.profileId(), user.online()));
      }
    }
    return ResponseEntity.ok().body(ApiResponse.success(dtoList));
  }

  @Tag(name = "특정 User의 온라인 상태 수정")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<ApiResponse<UserStatus>> update(@PathVariable("userId") UUID userId) {
    UserStatus status = userStatusService.updateByUserId(userId);
    return ResponseEntity.ok().body(ApiResponse.success(status));
  }

}
