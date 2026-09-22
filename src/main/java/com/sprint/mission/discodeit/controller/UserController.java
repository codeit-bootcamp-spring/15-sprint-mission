package com.sprint.mission.discodeit.controller;
import java.io.IOException;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import org.springframework.web.bind.annotation.PathVariable;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping( method = RequestMethod.GET)
    public List<UserDto> findAll(@RequestBody UserCreateRequest request) {

        return userService.findAll();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public UserDto update(
            @PathVariable UUID id,
            @RequestBody UserUpdateRequest request
            ) {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                id, request.getUsername(), request.getProfileImageId()
        );

        return userService.update(request).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) throws IOException {
        userService.delete(id);
    }


}
