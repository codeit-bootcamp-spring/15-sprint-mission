package com.sprint.mission.discodeit.service.basic;

import static aQute.bnd.annotation.headers.Category.users;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.base.BinaryContent;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  BinaryContentRepository binaryContentRepository;
  @Mock
  UserStatusRepository userStatusRepository;
  @Mock
  UserMapper userMapper;
  @Mock
  BinaryContentStorage binaryContentStorage;

  @InjectMocks
  BasicUserService userService;

  @Test
  @DisplayName("프로필 없이 유저를 생성하면 유저가 저장된다")
  void user_create_success_case01() {
    // given
    UserCreateRequest request = new UserCreateRequest("username", "user@naver.com", "password");
    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(userRepository.existsByUsername(request.username())).thenReturn(false);
    // when
    userService.create(request, null);
    // then
    verify(userRepository).save(any(User.class));
  }


  @Test
  @DisplayName("프로필과 함께 유저를 생성하면 프로필이 연결된 유저가 저장된다")
  void user_create_success_case02() {
    // given
    UserCreateRequest request = new UserCreateRequest("username", "user@naver.com", "password");

    byte[] bytes = {1, 2, 3};
    BinaryContentCreateRequest profile = new BinaryContentCreateRequest("profile.png", "image/png",
        bytes);

    UUID profileId = UUID.randomUUID();
    BinaryContent savedProfile = mock(BinaryContent.class);

    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(userRepository.existsByUsername(request.username())).thenReturn(false);
    when(binaryContentRepository.save(any(BinaryContent.class)))
        .thenReturn(savedProfile);
    when(savedProfile.getId()).thenReturn(profileId);

    // when
    userService.create(request, profile);

    // then
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertThat(savedUser.getProfile()).isSameAs(savedProfile);

    verify(binaryContentStorage).put(eq(profileId), aryEq(bytes));
  }

  @Test
  @DisplayName("유저 생성 실패 케이스 (이메일 중복)")
  void user_create_fail_case() {
    // given
    UserCreateRequest request = new UserCreateRequest("username", "user@naver.com", "password");
    when(userRepository.existsByEmail(request.email())).thenReturn(true);
    // when then
    assertThatThrownBy(() -> userService.create(request, null))
        .isInstanceOf(DuplicateEmailException.class);
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("유저 업데이트 성공 케이스")
  void user_update_success_case() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("username", "user@naver.com", "password", null);
    UserUpdateRequest updateUser = new UserUpdateRequest("newUsername", "newUser@naver.com",
        "newPassword");
    UserDto userDto = new UserDto(userId, updateUser.newUsername(), updateUser.newEmail(), null,
        true);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.toDto(user)).thenReturn(userDto);
    // when
    UserDto result = userService.update(userId, updateUser, null);
    // then
    assertThat(user.getUsername()).isEqualTo(updateUser.newUsername());
    assertThat(user.getEmail()).isEqualTo(updateUser.newEmail());
    verify(userRepository).findById(userId);
    assertThat(userDto).isEqualTo(result);
  }

  @Test
  @DisplayName("유저 업데이트 실패 케이스 (유저이름 중복)")
  void user_update_fail_case() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("username", "user@naver.com", "password", null);
    UserUpdateRequest updateUser = new UserUpdateRequest("username", null, null);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.existsByUsername(updateUser.newUsername())).thenReturn(true);
    // when then
    assertThatThrownBy(() -> userService.update(userId, updateUser, null)).isInstanceOf(
        DuplicateUsernameException.class);
    assertThat(user.getUsername()).isEqualTo("username");
  }

  @Test
  @DisplayName("유저 삭제 성공 케이스")
  void user_delete_success_case() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("username", "user@naver.com", "password", null);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    // when
    userService.delete(userId);
    verify(userRepository).deleteById(userId);
  }
}
















