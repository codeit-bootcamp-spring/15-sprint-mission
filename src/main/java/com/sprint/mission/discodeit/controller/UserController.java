package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final BasicUserService userService;
    public UserController(BasicUserService userService){
        this.userService = userService;
    }

    // 생성
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @ModelAttribute UserCreateRequest userCreateRequest,
            @RequestPart Optional<BinaryContentCreateRequest> binaryContentCreateRequest
            ) throws IOException {
        User user = userService.create(userCreateRequest, binaryContentCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(UserResponse.from(user)));
    }
    // 조회
    @RequestMapping(value="/{user-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
        @PathVariable("user-id") UUID userId) {

        UserDto userDto = userService.find(userId);

        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(userDto)));
    }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> users = userService.findAll().stream()
                .map(UserResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    // 수정
    @RequestMapping(value="/{user-id}",method=RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable("user-id") UUID userId,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            @RequestPart Optional<BinaryContentCreateRequest> binaryContentCreateRequest

    ) throws IOException {
        User update = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(update)));
    }
    // 삭제
    @RequestMapping(value="/{user-id}",method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
