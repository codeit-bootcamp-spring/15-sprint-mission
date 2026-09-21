package com.sprint.mission.discodeit.controller;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDto> create(
            @RequestBody UserCreateRequest request
    ) {
        UserDto user = userService.create(request);

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

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<UserDto> update(
            @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(
                userService.update(request)
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
            path = "/{userId}/status",
            method = RequestMethod.PATCH
    )
    public ResponseEntity<UserStatusDto> updateStatus(
            @PathVariable("userId") UUID userId,
            @RequestBody UserStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(
                userStatusService.updateByUserId(
                        userId,
                        request.lastActiveAt()
                )
        );
    }
}
