package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserFindAllController {

    private final UserService userService;

    // 사용자 목록 조회
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.readAll().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    private UserDto toDto(UserResponse response) {
        return new UserDto(
                response.id(),
                response.createdAt(),
                response.updatedAt(),
                response.userName(),
                response.email(),
                response.profileId(),
                response.online()
        );
    }
}