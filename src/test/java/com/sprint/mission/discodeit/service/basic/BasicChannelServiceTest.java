package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
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
class BasicChannelServiceTest {
    @Mock ChannelRepository channelRepository;
    @Mock MessageRepository messageRepository;
    @Mock ReadStatusRepository readStatusRepository;
    @Mock BinaryContentRepository binaryContentRepository;
    @Mock UserRepository userRepository;
    @Mock ChannelMapper channelMapper;
    @InjectMocks BasicChannelService channelService;

    UUID channelId;
    Channel channel;
    ChannelDto expected;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        channel = new Channel("test", "description", ChannelType.PUBLIC);
        ReflectionTestUtils.setField(channel, "id", channelId);
        expected = new ChannelDto(channelId, "test", "description", ChannelType.PUBLIC,
                List.of(), Instant.now());
    }

    @Test
    @DisplayName("공개 채널 생성 성공")
    void 공개_채널_생성_성공_01() {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "description");
        when(channelMapper.toDto(any(Channel.class), anyList(), any(Instant.class))).thenReturn(expected);

        // when
        ChannelDto result = channelService.create(request);

        // then
        assertSame(expected, result);
        verify(channelRepository).save(argThat(saved -> saved.getType() == ChannelType.PUBLIC
                && saved.getName().equals(request.name())));
    }

    @Test
    @DisplayName("비공개 채널 생성 성공")
    void 비공개_채널_생성_성공_01() {
        // given
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(UUID.randomUUID()));
        User user = new User("user@example.com", "password", "username", null);
        ChannelDto privateDto = new ChannelDto(channelId, null, null, ChannelType.PRIVATE,
                List.of(), Instant.now());
        when(userRepository.findAllByIdIn(request.participantIds())).thenReturn(List.of(user));
        when(channelMapper.toDto(any(Channel.class), anyList(), any(Instant.class))).thenReturn(privateDto);

        // when
        ChannelDto result = channelService.create(request);

        // then
        assertSame(privateDto, result);
        verify(channelRepository).save(argThat(saved -> saved.getType() == ChannelType.PRIVATE));
        verify(readStatusRepository).save(any(ReadStatus.class));
    }


    @Test
    @DisplayName("채널 수정 성공")
    void 채널_수정_성공_01() {
        // given
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("updated", "updated description");
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(channelMapper.toDto(eq(channel), anyList(), any(Instant.class))).thenReturn(expected);

        // when
        ChannelDto result = channelService.update(channelId, request);

        // then
        assertSame(expected, result);
        assertEquals(request.name(), channel.getName());
        assertEquals(request.description(), channel.getDescription());
    }

    @Test
    @DisplayName("비공개 채널 수정 실패")
    void 채널_수정_실패_01() {
        // given
        Channel privateChannel = new Channel(null, null, ChannelType.PRIVATE);
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("updated", "description");
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(privateChannel));

        // when & then
        assertThatThrownBy(() -> channelService.update(channelId, request))
                .isInstanceOf(PrivateChannelUpdateException.class);
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void 채널_삭제_성공_01() {
        // given
        when(channelRepository.existsById(channelId)).thenReturn(true);

        // when
        channelService.delete(channelId);

        // then
        verify(channelRepository).deleteById(channelId);
    }

    @Test
    @DisplayName("존재하지 않는 채널 삭제 실패")
    void 채널_삭제_실패_01() {
        // given
        when(channelRepository.existsById(channelId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> channelService.delete(channelId))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @Test
    @DisplayName("유저별 채널 목록 조회 성공")
    void 유저별_채널_조회_성공_01() {
        // given
        UUID userId = UUID.randomUUID();
        when(channelRepository.findAllVisibleToUser(userId, ChannelType.PUBLIC, ChannelType.PRIVATE))
                .thenReturn(List.of(channel));
        when(channelMapper.toDto(eq(channel), anyList(), any(Instant.class))).thenReturn(expected);

        // when
        List<ChannelDto> result = channelService.findAllByUserId(userId);

        // then
        assertEquals(List.of(expected), result);
    }


}
