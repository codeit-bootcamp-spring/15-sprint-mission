package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.service.basic.BasicUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Tag(name="User", description = "User API")
@RequestMapping("/api/users")
@RestController
public class UserController {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");
    private final BasicUserService userService;
    public UserController(BasicUserService userService){
        this.userService = userService;
    }


    private void validateImageFile(MultipartFile file) {
        // 1차 방어: 파일 선택 없이 폼만 제출한 경우
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        // 2차 방어: 확장자 화이트리스트 검사 (파일명 문자열 기반이라 조작 가능)
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 확장자입니다: " + extension);
        }

        // 3차 방어: 실제 Content-Type 검사 (확장자만 바꿔치기한 파일을 걸러내는 2중 검증)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    // 생성
    @Operation(summary = "User 등록", operationId = "create")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨")
    })
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @ModelAttribute UserCreateRequest userCreateRequest,
            @RequestPart(value="image", required = false) MultipartFile file
            ) throws IOException {
        // 파일 검증
        validateImageFile(file);
        // 파일명과 확장자 만들기
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + "." + extension;
        // 디스크 저장
        Path savePath = Paths.get("./uploads/" + savedFileName);
        Files.createDirectories(savePath.getParent());
        file.transferTo(savePath);


        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
        Optional<BinaryContentCreateRequest> binaryContentCreateRequest
                = Optional.of(request);

        User user = userService.create(userCreateRequest, binaryContentCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(UserResponse.from(user)));
    }
    // 조회
    @Operation(summary = "User 조회", operationId = "")
    @RequestMapping(value="/{user-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
        @PathVariable("user-id") UUID userId) {

        UserDto userDto = userService.find(userId);

        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(userDto)));
    }


    // 정적 리소스 서빙
    @Operation(summary = "전체 User 목록 조회", operationId = "findAll")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User 목록 조회 성공")
    })
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    // 수정
    @Operation(summary = "User 정보 수정", operationId = "update")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User를 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨")
    })
    @RequestMapping(value="/{user-id}",method=RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable("user-id") UUID userId,
            @Valid @ModelAttribute UserUpdateRequest userUpdateRequest,
            @RequestPart(value="image", required = false) MultipartFile file

    ) throws IOException {
        // 파일 검증
        validateImageFile(file);
        // 파일명과 확장자 만들기
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + "." + extension;
        // 디스크 저장
        Path savePath = Paths.get("./uploads/" + savedFileName);
        Files.createDirectories(savePath.getParent());
        file.transferTo(savePath);
        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
        Optional<BinaryContentCreateRequest> binaryContentCreateRequest
                = Optional.of(request);

        User update = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(update)));
    }
    // 삭제
    @Operation(summary = "User 삭제", operationId = "delete")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
    })
    @RequestMapping(value="/{user-id}",method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
