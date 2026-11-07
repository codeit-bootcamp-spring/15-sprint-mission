package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.exception.BinaryRequestFailedException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Tag(name = "User", description = "User API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final Validator validator;

    //유저 생성
    @Operation(
            summary = "User 등록",
            operationId = "create"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "유저 등록 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이름이나 이메일 중복됨"
            ),

    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestPart("userCreateRequest") @Valid UserCreateRequest userCreateRequest,
                                              @RequestPart(value = "profile", required = false) MultipartFile profile)throws IOException {
        log.debug("회원가입 요청");
        Optional<BinaryContentCreateRequest> binaryRequest =
                Optional.ofNullable(profile)
                        .map(file -> {
                            try {
                                BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                                var violations = validator.validate(request);
                                if (!violations.isEmpty()) {
                                    throw new ConstraintViolationException(violations);
                                }
                                return request;
                            } catch (IOException e) {
                                throw new BinaryRequestFailedException(e,profile.getOriginalFilename());
                            }
                        });

        UserDto userDto = userService.create(userCreateRequest, binaryRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userDto);
    }

    //유저 수정
    @Operation(
            summary = "User 정보 수정",
            operationId = "update"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "유저 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "같은 email 또는 username를 사용하는 User가 이미 존재함"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없음"
            )
    })
    @PatchMapping(path = "/{user-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> patchUser(
            @PathVariable("user-id") UUID uuid, @RequestPart("userUpdateRequest") @Valid UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

        log.debug("회원정보 업데이트 요청");
        Optional<BinaryContentCreateRequest> binaryRequest =
                Optional.ofNullable(profile)
                        .map(file -> {
                            try {
                                BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                                var violations = validator.validate(request);
                                if (!violations.isEmpty()) {
                                    throw new ConstraintViolationException(violations);
                                }
                                return request;
                            } catch (IOException e) {
                                throw new BinaryRequestFailedException(e,profile.getOriginalFilename());
                            }
                        });


        UserDto userDto = userService.update(uuid, userUpdateRequest, binaryRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDto);
    }

    //삭제
    @Operation(
            summary = "User 삭제",
            operationId = "delete"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "유저 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없음"
            )
    })
    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") UUID uuid) {
        //UUID uuid = UUID.fromString(Id);
        log.debug("탈퇴 요청");
        userService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    //전체 조회
    @Operation(
            summary = "전체 User 목록 조회",
            operationId = "findAll"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 목록 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        log.debug("전체 회원정보 조회 요청");
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }



    @Operation(
            summary = "User 온라인 상태 업데이트",
            operationId = "updateUserStatusByUserId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "유저 온라인 상태 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저의 UserStatus를 찾을 수 없음"
            )
    })
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
                                                                  @RequestBody @Valid UserStatusUpdateRequest userStatusUpdateRequest){
        UserStatusDto userStatusDto = userStatusService.updateByUserId(userId,userStatusUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userStatusDto);
    }


}
