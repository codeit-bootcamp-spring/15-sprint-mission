package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    //유저 생성
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        User user = userService.create(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.toUserResponse(user));
    }

    //유저 수정
    @PatchMapping("/{user-id}")
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable("user-id") UUID uuid, @RequestBody UserUpdateRequest userUpdateRequest) {




        User user = userService.update(uuid, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.toUserResponse(user));
    }

    //삭제
    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") UUID uuid) {
        //UUID uuid = UUID.fromString(Id);
        userService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    //전체 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(users);
    }


}
