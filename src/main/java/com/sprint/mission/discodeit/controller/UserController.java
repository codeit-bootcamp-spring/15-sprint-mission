package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    //유저 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(
            responseCode = "201",
            description = "User 등록 성공"
    )
    public ResponseEntity<UserDto> createUser(@RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
                                              @RequestPart(value = "profile", required = false) MultipartFile profile)throws IOException {
        Optional<BinaryContentCreateRequest> binaryRequest =
                Optional.ofNullable(profile)
                        .map(file -> {
                            try {
                                return new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });

        User user = userService.create(userCreateRequest, binaryRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.toUserResponse(user));
    }

    //유저 수정
    @PatchMapping(path = "/{user-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> patchUser(
            @PathVariable("user-id") UUID uuid, @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest) {




        User user = userService.update(uuid, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.toUserResponse(user));
    }

    //삭제
    @ApiResponse(
            responseCode = "204",
            description = "유저 삭제 성공"
    )
    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") UUID uuid) {
        //UUID uuid = UUID.fromString(Id);
        userService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    //전체 조회
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable("userId") UUID userId){
        UserStatus userStatus=userStatusService.updateByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);
    }


}
