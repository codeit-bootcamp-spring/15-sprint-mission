package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {
    @Mock MessageRepository messageRepository;
    @Mock ChannelRepository channelRepository;
    @Mock UserRepository userRepository;
    @Mock BinaryContentRepository binaryContentRepository;
    @Mock BinaryContentStorage binaryContentStorage;
    @Mock MessageMapper messageMapper;
    @InjectMocks BasicMessageService messageService;

    UUID channelId;
    UUID userId;
    UUID messageId;
    Channel channel;
    User user;
    Message message;
    MessageDto expected;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId = UUID.randomUUID();
        messageId = UUID.randomUUID();
        channel = new Channel("test", "description", ChannelType.PUBLIC);
        user = new User("user@example.com", "password", "username", null);
        message = new Message(channel, user, "hello", List.of());
        Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
        ReflectionTestUtils.setField(message, "createdAt", createdAt);
        expected = new MessageDto(messageId, createdAt, null, "hello", channelId, null, List.of());
    }

    @Test
    @DisplayName("메시지 생성 성공")
    void 메시지_생성_성공_01() {
        // given
        MessageCreateRequest request = new MessageCreateRequest(channelId, userId, "hello");
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(messageMapper.toDto(any(Message.class))).thenReturn(expected);

        // when
        MessageDto result = messageService.create(request, List.of());

        // then
        assertSame(expected, result);
        verify(messageRepository).save(argThat(saved -> saved.getContent().equals(request.content())));
    }

    @Test
    @DisplayName("존재하지 않는 채널에 메시지 생성 실패")
    void 메시지_생성_실패_01() {
        // given
        MessageCreateRequest request = new MessageCreateRequest(channelId, userId, "hello");
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.create(request, List.of()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @Test
    @DisplayName("메시지 수정 성공")
    void 메시지_수정_성공_01() {
        // given
        MessageUpdateRequest request = new MessageUpdateRequest("updated");
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageMapper.toDto(message)).thenReturn(expected);

        // when
        MessageDto result = messageService.update(messageId, request);

        // then
        assertSame(expected, result);
        assertEquals(request.newContent(), message.getContent());
    }

    @Test
    @DisplayName("존재하지 않는 메시지 수정 실패")
    void 메시지_수정_실패_01() {
        // given
        MessageUpdateRequest request = new MessageUpdateRequest("updated");
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.update(messageId, request))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void 메시지_삭제_성공_01() {
        // given
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // when
        messageService.delete(messageId);

        // then
        verify(messageRepository).delete(message);
    }

    @Test
    @DisplayName("존재하지 않는 메시지 삭제 실패")
    void 메시지_삭제_실패_01() {
        // given
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.delete(messageId))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    @DisplayName("채널별 메시지 목록 조회 성공")
    void 채널별_메시지_조회_성공_01() {
        // given
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"));
        when(messageRepository.findAllByChannelIdWithCursor(channelId, null, pageable))
                .thenReturn(new SliceImpl<>(List.of(message), pageable, true));
        when(messageMapper.toDto(message)).thenReturn(expected);
        when(messageRepository.countByChannelId(channelId)).thenReturn(2L);

        // when
        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

        // then
        assertEquals(List.of(expected), result.content());
        assertEquals(message.getCreatedAt().toString(), result.nextCursor());
        assertTrue(result.hasNext());
        assertEquals(2L, result.totalElements());
    }

    @Test
    @DisplayName("저장소 오류로 채널별 메시지 목록 조회 실패")
    void 채널별_메시지_조회_실패_01() {
        // given
        Pageable pageable = PageRequest.of(0, 50);
        when(messageRepository.findAllByChannelIdWithCursor(channelId, null, pageable))
                .thenThrow(new DataAccessResourceFailureException("DB connection failed"));

        // when & then
        assertThatThrownBy(() -> messageService.findAllByChannelId(channelId, null, pageable))
                .isInstanceOf(DataAccessResourceFailureException.class);
    }
}
