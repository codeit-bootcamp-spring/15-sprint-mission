package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import org.springframework.web.multipart.MultipartFile;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserController(
            UserService userService,
            UserStatusService userStatusService
    ) {
        this.userService = userService;
        this.userStatusService = userStatusService;
    }

    @RequestMapping(
        method = RequestMethod.POST,
        consumes = "multipart/form-data"
    )
    public ResponseEntity<UserDto> create(
        @RequestPart("userCreateRequest") UserCreateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws Exception {

        BinaryContentCreateRequest profileRequest = null;

        if (profile != null && !profile.isEmpty()) {
            profileRequest = new BinaryContentCreateRequest(
                profile.getOriginalFilename(),
                profile.getContentType(),
                profile.getBytes()
            );
        }

        UserCreateRequest userCreateRequest = new UserCreateRequest(
            request.username(),
            request.email(),
            request.password(),
            profileRequest
        );

        UserDto user = userService.create(userCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(
                userService.findAll()
        );
    }

    @RequestMapping(
        path = "/{userId}",
        method = RequestMethod.PATCH,
        consumes = "multipart/form-data"
    )
    public ResponseEntity<UserDto> update(
        @PathVariable("userId") UUID userId,
        @RequestPart("userUpdateRequest") UserUpdateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws Exception {

        System.out.println("UPDATE username = " + request.newUsername());
        System.out.println("UPDATE email = " + request.newEmail());
        System.out.println("UPDATE profile = " +
            (profile == null ? "NULL" : profile.getOriginalFilename()));

        BinaryContentCreateRequest profileRequest = null;

        if (profile != null && !profile.isEmpty()) {
            profileRequest = new BinaryContentCreateRequest(
                profile.getOriginalFilename(),
                profile.getContentType(),
                profile.getBytes()
            );
        }

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
            request.newUsername(),
            request.newEmail(),
            request.newPassword(),
            profileRequest
        );

        return ResponseEntity.ok(
            userService.update(userId, userUpdateRequest)
        );
    }
    @RequestMapping(
            path = "/{userId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<Void> delete(
            @PathVariable("userId") UUID userId
    ) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
        path = "/{userId}/userStatus",
        method = RequestMethod.PATCH
    )
    public ResponseEntity<UserStatusDto> updateStatus(
            @PathVariable("userId") UUID userId,
            @RequestBody UserStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(
            userStatusService.updateByUserId(
                userId,
                Instant.now()
            )
        );
    }
}
