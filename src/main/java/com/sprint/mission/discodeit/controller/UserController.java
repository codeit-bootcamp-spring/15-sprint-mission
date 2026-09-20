package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponse> createUser(
            @RequestParam("username") String userName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "image", required = false) MultipartFile image)  {
        UserCreateRequest request = new UserCreateRequest(userName, email, password);

        BinaryContentCreateRequest contentCreateRequest = null;
        try {
            if (!image.isEmpty()) {
                contentCreateRequest = new BinaryContentCreateRequest(
                        image.getOriginalFilename(),
                        image.getContentType(),
                        image.getBytes());
            }

            User user = userService.create(request, contentCreateRequest);
            return ResponseEntity.ok().body(userService.find(user.getId()));
        } catch (IOException e) {
            throw new RuntimeException("쓰기 실패");
        }
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<UserResponse> updateUser(
            @RequestParam("id") UUID id,
            @RequestParam("username") String userName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("profile-id") UUID profileId,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        UserUpdateRequest request = new UserUpdateRequest(id, userName, email, password, profileId);

        BinaryContentUpdateRequest contentUpdateRequest = null;
        try {
            if (!image.isEmpty()) {
                contentUpdateRequest = new BinaryContentUpdateRequest(
                        image.getOriginalFilename(),
                        image.getContentType(),
                        image.getBytes());
            }
            return  ResponseEntity.ok().body(userService.update(request, contentUpdateRequest));
        } catch (IOException e) {
            throw new RuntimeException("쓰기 실패");
        }

    }

    @RequestMapping(path = "/{user-id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") UUID id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(path = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateOnline(@PathVariable("id") UUID id) {
        userStatusService.updateByUserId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
