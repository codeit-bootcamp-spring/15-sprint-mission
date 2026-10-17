package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserResponseDto create(UserCreateDto dto);

  UserResponseDto find(UUID id);

  List<UserResponseDto> findAll();

  UserResponseDto update(UUID id, UserUpdateDto dto); // 👈 인자 2개 (id, dto)

  void delete(UUID id);
}