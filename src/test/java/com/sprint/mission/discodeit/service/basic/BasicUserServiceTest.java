package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  UserMapper userMapper;

  @InjectMocks
  BasicUserService userService;


  @Test
  void 사용자_생성_테스트() {

    //given - 사용자 생성
    UserCreateRequest request = new UserCreateRequest("최주영", "test@test.com", "123456");

    // when - 사용자 생성 실행
    userService.create(request, Optional.empty());

    // then - 저장 메서드가 호출되었는지 확인
    then(userRepository).should().save(any(User.class));
  }

  @Test
  void 사용자_생성_실패_이메일중복() {

    // given - 이미 등록된 이메일이라고 가정
    UserCreateRequest request =
        new UserCreateRequest("홍길동", "test@test.com", "123456");

    given(userRepository.existsByEmail("test@test.com"))
        .willReturn(true);

    // when & then - 사용자 생성 시 예외가 발생하는지 확인
    assertThrows(UserAlreadyExistsException.class,
        () -> userService.create(request, Optional.empty()));
  }

  @Test
  void 사용자_데이터_수정_성공_테스트() {

    // given - 기존 사용자와 수정할 정보 준비
    UUID userId = UUID.randomUUID();
    User user = mock(User.class);

    UserUpdateRequest request =
        new UserUpdateRequest("수정된이름", "new@test.com", "123456");

    given(userRepository.findById(userId))
        .willReturn(Optional.of(user));

    // when & then - 수정이 오류 없이 실행되는지 확인
    assertDoesNotThrow(() ->
        userService.update(userId, request, Optional.empty()));
  }

  @Test
  void 사용자_수정_실패() {

    // given - 존재하지 않는 사용자
    UUID userId = UUID.randomUUID();

    UserUpdateRequest request =
        new UserUpdateRequest("홍길동", "test@test.com", "123456");

    given(userRepository.findById(userId))
        .willReturn(Optional.empty());

    // when & then - 수정 시 예외 발생 확인
    assertThrows(UserNotFoundException.class,
        () -> userService.update(userId, request, Optional.empty()));
  }

  @Test
  void 사용자_삭제_성공() {

    // given - 사용자가 존재한다고 가정
    UUID userId = UUID.randomUUID();

    given(userRepository.existsById(userId))
        .willReturn(true);

    // when - 사용자 삭제 실행
    userService.delete(userId);

    // then - 삭제 메서드가 호출되었는지 확인
    then(userRepository).should().deleteById(userId);
  }

  @Test
  void 사용자_삭제_실패() {

    // given - 사용자가 존재하지 않는다고 가정
    UUID userId = UUID.randomUUID();

    given(userRepository.existsById(userId))
        .willReturn(false);

    // when & then - 삭제 시 예외가 발생하는지 확인
    assertThrows(UserNotFoundException.class,
        () -> userService.delete(userId));
  }

}