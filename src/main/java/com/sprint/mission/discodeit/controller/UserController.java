package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.ApiResponse;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    //유저 생성
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        User user = userService.create(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userService.toUserResponse(user)));
    }

    //유저 수정
    @PatchMapping("/{user-id}")
    public ResponseEntity<ApiResponse<UserResponse>> patchUser(
            @PathVariable("user-id") UUID uuid, @RequestBody UserUpdateRequest userUpdateRequest) {

        //패스워드는 Response DTO에 없는 필드여서 Service의 update에서 방어했습니다.
        String email;
        String name;
        NitroLevel nitroLevel;
        Optional<UUID> profileId;

        UserResponse finduser = userService.find(uuid);
        if(userUpdateRequest.email() != null) {
            email = userUpdateRequest.email();
        }else {
            email = finduser.email();
        }
        if(userUpdateRequest.name() != null) {
            name = userUpdateRequest.name();
        }else {
            name = finduser.name();
        }
        if(userUpdateRequest.nitroLevel() != null) {
            nitroLevel = userUpdateRequest.nitroLevel();
        }else {
            nitroLevel = finduser.nitroLevel();
        }
        if(userUpdateRequest.profileId() != null) {
            profileId = userUpdateRequest.profileId();
        }else {
            profileId = finduser.profileId();
        }

        UserUpdateRequest request = new UserUpdateRequest(email,userUpdateRequest.password(),name,nitroLevel,profileId);

        User user = userService.update(uuid, request);
        return ResponseEntity.ok(ApiResponse.success(userService.toUserResponse(user)));
    }

    //삭제
    @DeleteMapping("/{user-id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("user-id") UUID uuid) {
        //UUID uuid = UUID.fromString(Id);
        userService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    //전체 조회
    @GetMapping("/findAll")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }


}
