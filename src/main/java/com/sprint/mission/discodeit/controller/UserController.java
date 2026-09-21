package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateBody;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<User> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile image) {

    BinaryContentCreateRequest contentCreateRequest = null;
    try {
      if (image != null && !image.isEmpty()) {
        contentCreateRequest = new BinaryContentCreateRequest(
            image.getOriginalFilename(),
            image.getContentType(),
            image.getBytes());
      }

      User user = userService.create(request, contentCreateRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
    } catch (IOException e) {
      throw new RuntimeException("쓰기 실패");
    }
  }

  @RequestMapping(path = "/{userId}", method = RequestMethod.PATCH)
  public ResponseEntity<User> updateUser(
      @PathVariable("userId") UUID id,
      @RequestPart("userUpdateRequest") UserUpdateBody body,
      @RequestPart(value = "profile", required = false) MultipartFile image) {
    UserUpdateRequest request = new UserUpdateRequest(
        id, body.newUsername(), body.newEmail(), body.newPassword());

    BinaryContentUpdateRequest contentUpdateRequest = null;
    try {
      if (image != null && !image.isEmpty()) {
        contentUpdateRequest = new BinaryContentUpdateRequest(
            image.getOriginalFilename(),
            image.getContentType(),
            image.getBytes());
      }
      return ResponseEntity.ok().body(userService.update(request, contentUpdateRequest));
    } catch (IOException e) {
      throw new RuntimeException("쓰기 실패");
    }

  }

  @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id) {
    userService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok().body(userService.findAll());
  }

  @RequestMapping(path = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatus> updateOnline(
      @PathVariable("userId") UUID id,
      @RequestBody UserStatusUpdateRequest request) {
    return ResponseEntity.ok().body(userStatusService.updateByUserId(id, request.newLastActiveAt()));
  }
}
