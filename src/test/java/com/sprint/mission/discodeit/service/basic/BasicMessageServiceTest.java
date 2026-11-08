package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  MessageRepository messageRepository;
  @Mock
  BinaryContentRepository binaryContentRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  ChannelRepository channelRepository;
  @Mock
  MessageMapper messageMapper;
  @Mock
  BinaryContentStorage binaryContentStorage;
  @Mock
  PageResponseMapper pageResponseMapper;
  @InjectMocks
  BasicMessageService messageService;

  @Test
  @DisplayName("메세지 생성 성공 케이스")
  void message_create_success_case() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "channelName", "channelDesc");
    User user = new User("username", "user@naver.com", "password", null);
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channelId, authorId,
        "content");
    Message message = new Message("content", channel, user, List.of());
    UserDto userDto = new UserDto(authorId, "username", "user@naver.com", null, true);
    MessageDto messageDto = new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(),
        "content", channelId, userDto, null);
    when(channelRepository.existsById(channelId)).thenReturn(true);
    when(userRepository.existsById(authorId)).thenReturn(true);
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(userRepository.findById(authorId)).thenReturn(Optional.of(user));
    when(messageRepository.save(any(Message.class))).thenReturn(message);
    when(messageMapper.toDto(message)).thenReturn(messageDto);
    // when
    MessageDto result = messageService.create(messageCreateRequest, null);
    // then
    assertThat(result.content()).isEqualTo("content");
  }

  @Test
  @DisplayName("메세지 생성 실패케이스")
  void message_create_fail_case() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    MessageCreateRequest message = new MessageCreateRequest(channelId, userId, "content");
    when(channelRepository.existsById(channelId)).thenReturn(true);
    when(userRepository.existsById(userId)).thenReturn(false);
    // when then
    assertThatThrownBy(() -> messageService.create(message, List.of())).isInstanceOf(
        UserNotFoundException.class);
  }

  @Test
  @DisplayName("메세지 수정 성공 케이스")
  void message_update_success_case() {
    // given
    UUID messageId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    User user = new User("username", "user@naver.com", "password", null);
    UserDto userDto = new UserDto(authorId, "username", "user@naver.com", null, true);
    Channel channel = new Channel(ChannelType.PUBLIC, "channelName", "channelDesc");
    Message message = new Message("content", channel, user, List.of());
    MessageDto dto = new MessageDto(messageId, null, null, "newContent", channelId, userDto,
        List.of());
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    when(messageMapper.toDto(message)).thenReturn(dto);
    when(messageRepository.save(message)).thenReturn(message);
    // when
    MessageDto messageDto = messageService.update(messageId, request);
    // then
    assertThat(messageDto.content()).isEqualTo("newContent");
  }

  @Test
  @DisplayName("메세지 수정 실패 케이스")
  void message_update_fail_case() {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
    // when then
    assertThatThrownBy(() -> messageService.update(messageId, request)).isInstanceOf(
        MessageNotFoundException.class);
  }

  @Test
  @DisplayName("메세지 삭제 성공 케이스")
  void message_delete_success_case() {
    // given
    UUID messageId = UUID.randomUUID();
    User user = new User("username", "user@naver.com", "password", null);
    Channel channel = new Channel(ChannelType.PUBLIC, "channelName", "channelDesc");
    Message message = new Message("content", channel, user, List.of());
    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    // when
    messageService.delete(messageId);
    // then
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  @DisplayName("메세지 삭제 실패 케이스")
  void message_delete_fail_case() {
    // given
    UUID messageId = UUID.randomUUID();
    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
    // when then
    assertThatThrownBy(() -> messageService.delete(messageId)).isInstanceOf(
        MessageNotFoundException.class);
  }

  @Test
  @DisplayName("특정 채널 내 모든 메세지 호출 성공 케이스")
  void message_findAll_by_channel() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "channelName", "channelDesc");
    User user = new User("username", "user@naver.com", "password", null);
    Message message = new Message("content", channel, user, List.of());
    MessageDto messageDto = new MessageDto(UUID.randomUUID(), Instant.now(), null, "content",
        channelId, null, List.of()
    );

    Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
    Slice<Message> messageSlice = new SliceImpl<>(List.of(message), pageable, false);
    Slice<MessageDto> dtoSlice = new SliceImpl<>(List.of(messageDto), pageable, false);
    PageResponse<MessageDto> expected = new PageResponse<>(List.of(messageDto), null, 20, false,
        null);

    when(messageRepository.findAllByChannelId(channelId, pageable)).thenReturn(messageSlice);
    when(messageMapper.toDto(message)).thenReturn(messageDto);
    when(pageResponseMapper.fromSlice(eq(dtoSlice), any())).thenReturn(expected);

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

    // then
    verify(messageRepository).findAllByChannelId(channelId, pageable);
    verify(messageMapper).toDto(message);
    assertThat(result).isSameAs(expected);
    assertThat(result.content()).containsExactly(messageDto);
  }

}