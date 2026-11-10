
package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  MessageRepository messageRepository;

  @Mock
  ChannelRepository channelRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  MessageMapper messageMapper;

  @Mock
  BinaryContentStorage binaryContentStorage;

  @InjectMocks
  BasicMessageService messageService;

  @Mock
  PageResponseMapper pageResponseMapper;


  // STEP 1-1. 메시지 생성 성공
  @Test
  void 메시지_생성_성공() {

    // given - 채널과 작성자가 존재한다고 가정
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateRequest request =
        new MessageCreateRequest("안녕하세요", channelId, authorId);

    given(channelRepository.findById(channelId))
        .willReturn(Optional.of(mock(Channel.class)));

    given(userRepository.findById(authorId))
        .willReturn(Optional.of(mock(User.class)));

    // when - 메시지 생성
    messageService.create(request, List.of());

    // then - 저장 메서드 호출 확인
    then(messageRepository).should().save(any(Message.class));
  }


  // STEP 1-2. 메시지 생성 실패
  @Test
  void 메시지_생성_실패_채널없음() {

    // given - 존재하지 않는 채널
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateRequest request =
        new MessageCreateRequest("안녕하세요", channelId, authorId);

    given(channelRepository.findById(channelId))
        .willReturn(Optional.empty());

    // when & then - 예외 확인
    assertThrows(ChannelNotFoundException.class,
        () -> messageService.create(request, List.of()));
  }


  // STEP 2-1. 메시지 수정 성공
  @Test
  void 메시지_수정_성공() {

    // given - 기존 메시지
    UUID messageId = UUID.randomUUID();

    Message message = new Message(
        "수정 전",
        mock(Channel.class),
        mock(User.class),
        List.of()
    );

    MessageUpdateRequest request =
        new MessageUpdateRequest("수정 후");

    given(messageRepository.findById(messageId))
        .willReturn(Optional.of(message));

    // when - 메시지 수정
    messageService.update(messageId, request);

    // then - 내용 변경 확인
    assertEquals("수정 후", message.getContent());
  }


  // STEP 2-2. 메시지 수정 실패
  @Test
  void 메시지_수정_실패() {

    // given - 존재하지 않는 메시지
    UUID messageId = UUID.randomUUID();

    MessageUpdateRequest request =
        new MessageUpdateRequest("수정 내용");

    given(messageRepository.findById(messageId))
        .willReturn(Optional.empty());

    // when & then - 예외 확인
    assertThrows(MessageNotFoundException.class,
        () -> messageService.update(messageId, request));
  }


  // STEP 3-1. 메시지 삭제 성공
  @Test
  void 메시지_삭제_성공() {

    // given - 메시지가 존재한다고 가정
    UUID messageId = UUID.randomUUID();

    given(messageRepository.existsById(messageId))
        .willReturn(true);

    // when - 메시지 삭제 실행
    messageService.delete(messageId);

    // then - 삭제 메서드 호출 확인
    then(messageRepository).should().deleteById(messageId);
  }


  // STEP 3-2. 메시지 삭제 실패
  @Test
  void 메시지_삭제_실패() {

    // given - 존재하지 않는 메시지
    UUID messageId = UUID.randomUUID();

    given(messageRepository.existsById(messageId))
        .willReturn(false);

    // when & then - 메시지를 찾을 수 없는 예외 확인
    assertThrows(MessageNotFoundException.class,
        () -> messageService.delete(messageId));
  }

  // STEP 4-1. 채널별 메시지 조회 성공
  @Test
  void 채널별_메시지_조회_성공() {

    // given - 메시지 1개 준비
    UUID channelId = UUID.randomUUID();
    Message message = mock(Message.class);
    MessageDto dto = mock(MessageDto.class);

    Instant cursor = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);

    given(messageRepository.findAllByChannelIdWithAuthor(
        channelId, cursor, pageable))
        .willReturn(new SliceImpl<>(List.of(message)));

    given(messageMapper.toDto(message))
        .willReturn(dto);

    given(dto.createdAt()).willReturn(cursor);

    // when - 메시지 조회 실행
    messageService.findAllByChannelId(channelId, cursor, pageable);

    // then - 조회 결과가 1개인지 확인
    then(pageResponseMapper).should().fromSlice(
        argThat(slice -> slice.getContent().size() == 1),
        eq(cursor)
    );
  }


  // STEP 4-2. 채널별 메시지 조회 빈 목록
  @Test
  void 채널별_메시지_조회_빈목록() {

    // given - 메시지가 없는 채널
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);

    given(messageRepository.findAllByChannelIdWithAuthor(
        eq(channelId), any(Instant.class), eq(pageable)))
        .willReturn(new SliceImpl<>(List.of()));

    // when - 메시지 조회 실행
    messageService.findAllByChannelId(channelId, null, pageable);

    // then - 빈 목록인지 확인
    then(pageResponseMapper).should().fromSlice(
        argThat(slice -> slice.getContent().isEmpty()),
        isNull()
    );
  }
}
