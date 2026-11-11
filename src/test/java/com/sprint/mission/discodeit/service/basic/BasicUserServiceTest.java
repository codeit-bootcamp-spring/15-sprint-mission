package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateUserEmailException;
import com.sprint.mission.discodeit.exception.DuplicateUserNameException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserStatusRepository userStatusRepository;
    @Mock
    BinaryContentRepository binaryContentRepository;
    @Mock
    BinaryContentStorage binaryContentStorage;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    BasicUserService userService;

    User user;
    UUID userid;
    @BeforeEach
    void setUp(){
        user = new User("kmgkmg@gmail.com", "qwer", "test1", null);
        userid = UUID.randomUUID();
    }


    @Test
    @DisplayName("유저 생성 성공")
    void 유저_생성_성공_01(){
        // given
        UserCreateRequest request = new UserCreateRequest("kmgkmg2@gmail.com", "qwer", "test2");
        UserDto expected = new UserDto(UUID.randomUUID(), "test2", "kmgkmg2@gmail.com", null, true);

        when(userMapper.toDto(any(User.class))).thenReturn(expected);

        // when
        UserDto result=userService.create(request, Optional.empty());

        // then
        assertEquals("kmgkmg2@gmail.com",result.email());
        assertEquals("test2",result.username());
        assertSame(expected, result);

    }





    @Test
    @DisplayName("username 중복으로 실패")
    void 유저_생성_실패_01(){
        // given
        UserCreateRequest request = new UserCreateRequest("new@gmail.com", "qwer", "test1");
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.create(request, Optional.empty()))
                .isInstanceOf(DuplicateUserNameException.class);
    }


    @Test
    @DisplayName("유저 업데이트 성공")
    void 유저_업데이트_성공_01(){
        // given
        UserUpdateRequest request = new UserUpdateRequest("updated@gmail.com", null, "updated");

        when(userRepository.findById(userid)).thenReturn(Optional.of(user));
        UserDto expected = new UserDto(userid, "updated", "updated@gmail.com", null, false);

        when(userMapper.toDto(user)).thenReturn(expected);

        // when
        UserDto result = userService.update(userid, request, Optional.empty());

        // then
        assertSame(expected, result);

    }

    @Test
    @DisplayName("중복된 이메일로 실패")
    void 유저_업데이트_실패_01(){
        // given
        UserUpdateRequest request = new UserUpdateRequest("duplicate@gmail.com", null, null);

        when(userRepository.findById(userid)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(request.newEmail(), userid)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.update(userid, request, Optional.empty()))
                .isInstanceOf(DuplicateUserEmailException.class);


    }

    @Test
    @DisplayName("유저 삭제 성공")
    void 유저_삭제_성공_01(){
        // given
        when(userRepository.findById(userid)).thenReturn(Optional.of(user));

        // when
        userService.delete(userid);

        // then
        verify(userRepository).findById(userid);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("없는 id")
    void 유저_삭제_실패_01(){
        // given
        when(userRepository.findById(userid)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.delete(userid))
                .isInstanceOf(UserNotFoundException.class);

    }




}
