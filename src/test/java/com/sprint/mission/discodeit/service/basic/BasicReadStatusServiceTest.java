
package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.exception.channel.*;
import com.sprint.mission.discodeit.exception.readStatus.*;
import com.sprint.mission.discodeit.exception.user.*;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicReadStatusServiceTest {

  @Mock
  ReadStatusRepository reads;
  @Mock
  UserRepository users;
  @Mock
  ChannelRepository channels;
  @Mock
  ReadStatusMapper readStatusMapper;
  @InjectMocks
  BasicReadStatusService service;

  UUID userId = UUID.randomUUID();
  UUID channelId = UUID.randomUUID();
  UUID readStatusId = UUID.randomUUID();
  Instant lastReadAt = Instant.parse("2026-11-09T00:00:00Z");

  private ReadStatusCreateRequest createRequest() {
    ReadStatusCreateRequest request = mock(ReadStatusCreateRequest.class);
    when(request.userId()).thenReturn(userId);
    when(request.channelId()).thenReturn(channelId);
    when(request.lastReadAt()).thenReturn(lastReadAt);
    return request;
  }

  @Test
  void create_success() {
    // given
    ReadStatusCreateRequest request = createRequest();
    User user = mock(User.class);
    Channel channel = mock(Channel.class);
    ReadStatusDto dto = mock(ReadStatusDto.class);

    when(users.findById(userId)).thenReturn(Optional.of(user));
    when(channels.findById(channelId)).thenReturn(Optional.of(channel));
    when(channel.getType()).thenReturn(Channel.ChannelType.PUBLIC);
    when(reads.findByUserIdAndChannelId(userId, channelId)).thenReturn(Optional.empty());
    when(reads.save(any(ReadStatus.class))).thenAnswer(inv -> inv.getArgument(0));
    when(readStatusMapper.toDto(any(ReadStatus.class))).thenReturn(dto);

    // when
    ReadStatusDto result = service.create(request);

    // then
    assertSame(dto, result);
    verify(reads).save(any(ReadStatus.class));
  }

  @Test
  void create_userNotFound() {
    // given
    ReadStatusCreateRequest request = createRequest();
    when(users.findById(userId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> service.create(request));
    verify(reads, never()).save(any(ReadStatus.class));
  }

  @Test
  void create_channelNotFound() {
    // given
    ReadStatusCreateRequest request = createRequest();
    when(users.findById(userId)).thenReturn(Optional.of(mock(User.class)));
    when(channels.findById(channelId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ChannelNotFoundException.class, () -> service.create(request));
    verify(reads, never()).save(any(ReadStatus.class));
  }

  @Test
  void create_privateChannelWithoutReadStatus() {
    // given
    ReadStatusCreateRequest request = createRequest();
    Channel channel = mock(Channel.class);

    when(users.findById(userId)).thenReturn(Optional.of(mock(User.class)));
    when(channels.findById(channelId)).thenReturn(Optional.of(channel));
    when(channel.getType()).thenReturn(Channel.ChannelType.PRIVATE);
    when(reads.findByUserIdAndChannelId(userId, channelId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(NotChannelParticipantException.class, () -> service.create(request));
    verify(reads, never()).save(any(ReadStatus.class));
  }

  @Test
  void create_alreadyExists() {
    // given
    ReadStatusCreateRequest request = createRequest();
    Channel channel = mock(Channel.class);

    when(users.findById(userId)).thenReturn(Optional.of(mock(User.class)));
    when(channels.findById(channelId)).thenReturn(Optional.of(channel));
    when(reads.findByUserIdAndChannelId(userId, channelId))
        .thenReturn(Optional.of(mock(ReadStatus.class)));

    // when & then
    assertThrows(ReadStatusAlreadyExistsException.class, () -> service.create(request));
    verify(reads, never()).save(any(ReadStatus.class));
  }

  @Test
  void find_success() {
    // given
    ReadStatus status = mock(ReadStatus.class);
    ReadStatusDto dto = mock(ReadStatusDto.class);

    when(reads.findById(readStatusId)).thenReturn(Optional.of(status));
    when(readStatusMapper.toDto(status)).thenReturn(dto);

    // when
    ReadStatusDto result = service.find(readStatusId);

    // then
    assertSame(dto, result);
  }

  @Test
  void find_notFound() {
    // given
    when(reads.findById(readStatusId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ReadStatusNotFoundException.class, () -> service.find(readStatusId));
  }

  @Test
  void findAllByUserId_success() {
    // given
    ReadStatus status = mock(ReadStatus.class);
    ReadStatusDto dto = mock(ReadStatusDto.class);

    when(reads.findAllByUserId(userId)).thenReturn(List.of(status));
    when(readStatusMapper.toDto(status)).thenReturn(dto);

    // when
    List<ReadStatusDto> result = service.findAllByUserId(userId);

    // then
    assertEquals(List.of(dto), result);
  }

  @Test
  void findAllByChannelId_success() {
    // given
    ReadStatus status = mock(ReadStatus.class);
    ReadStatusDto dto = mock(ReadStatusDto.class);

    when(reads.findAllByChannelId(channelId)).thenReturn(List.of(status));
    when(readStatusMapper.toDto(status)).thenReturn(dto);

    // when
    List<ReadStatusDto> result = service.findAllByChannelId(channelId);

    // then
    assertEquals(List.of(dto), result);
  }

  @Test
  void update_success() {
    // given
    ReadStatus status = mock(ReadStatus.class);
    ReadStatusDto dto = mock(ReadStatusDto.class);
    ReadStatusUpdateRequest request = mock(ReadStatusUpdateRequest.class);
    Instant newLastReadAt = lastReadAt.plusSeconds(60);

    when(reads.findById(readStatusId)).thenReturn(Optional.of(status));
    when(request.newLastReadAt()).thenReturn(newLastReadAt);
    when(reads.save(status)).thenReturn(status);
    when(readStatusMapper.toDto(status)).thenReturn(dto);

    // when
    ReadStatusDto result = service.update(readStatusId, request);

    // then
    assertSame(dto, result);
    verify(status).update(newLastReadAt);
    verify(reads).save(status);
  }

  @Test
  void update_notFound() {
    // given
    ReadStatusUpdateRequest request = mock(ReadStatusUpdateRequest.class);
    when(reads.findById(readStatusId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ReadStatusNotFoundException.class,
        () -> service.update(readStatusId, request));

    verify(reads, never()).save(any(ReadStatus.class));
  }

  @Test
  void delete_success() {
    // when
    service.delete(readStatusId);

    // then
    verify(reads).deleteById(readStatusId);
  }
}
