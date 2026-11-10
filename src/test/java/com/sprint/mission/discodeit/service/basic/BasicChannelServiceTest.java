package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  ChannelRepository channelRepository;

  @Mock
  ChannelMapper channelMapper;

  @Mock
  UserRepository userRepository;

  @Mock
  ReadStatusRepository readStatusRepository;

  @Mock
  MessageRepository messageRepository;

  @InjectMocks
  BasicChannelService channelService;

  @Test
  void 공개채널_생성_성공() {

    // 채널 생성
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("채널명", "설명");

    // 채널 생성 실행
    channelService.create(request);

    // 채널 생성 검증
    then(channelRepository).should().save(any());
  }

  @Test
  void 공개채널_생성_실패() {

    // given 채널 정보 준비
    PublicChannelCreateRequest request =
        new PublicChannelCreateRequest("채널명", "설명");

    // Repository 저장 실패 상황 가정
    given(channelRepository.save(any(Channel.class)))
        .willThrow(new DataIntegrityViolationException("저장 실패"));

    // when & then - 예외 발생 확인
    assertThrows(DataIntegrityViolationException.class,
        () -> channelService.create(request));
  }

  @Test
  void 비공개채널_생성_성공() {

    // given 참여자 2명 준비
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    // 채널 생성
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(userId1, userId2));

    // 채널 생성 실행
    channelService.create(request);

    // 채널 생성 검증
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  void 비공개채널_생성_실패_확인() {

    // given
    PrivateChannelCreateRequest request =
        new PrivateChannelCreateRequest(List.of());

    // when 발생한 예외를 변수에 저장
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class,
            () -> channelService.create(request));

    // then 예외 메시지 확인
    assertEquals("참여자가 없습니다.", exception.getMessage());
  }

  @Test
  void 공개채널_수정_성공_테스트() {

    // 기존 채널명
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "기존 채널명", "채널 설명");

    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("수정 채널명", "수정됐습니다.");

    given(channelRepository.findById(channelId)).willReturn(java.util.Optional.of(channel));

    channelService.update(channelId, request);

    // then - 실제 채널명이 변경되었는지 확인
    assertEquals("수정 채널명", channel.getName());
    assertEquals("수정됐습니다.", channel.getDescription());
  }

  @Test
  void 비공개채널_참여자_2명_생성_테스트() {

    // given - 참여자 2명 준비
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    PrivateChannelCreateRequest request =
        new PrivateChannelCreateRequest(List.of(userId1, userId2));

    given(userRepository.findAllById(List.of(userId1, userId2)))
        .willReturn(List.of(mock(User.class), mock(User.class)));

    // when - 비공개 채널 생성
    channelService.create(request);

    // then - 참여자 정보 저장 요청 확인
    then(readStatusRepository).should().saveAll(any());
  }

  @Test
  void 공개채널_삭제_테스트() {

    // 삭제할 채널
    UUID channelId = UUID.randomUUID();

    // 채널을 삭제할때
    given(channelRepository.existsById(channelId)).willReturn(true);

    // 실제 채널 삭제
    channelService.delete(channelId);

    // 채널 삭제 결과
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  void 공개채널_수정_실패_테스트() {

    // given - 존재하지 않는 채널
    UUID channelId = UUID.randomUUID();

    PublicChannelUpdateRequest request =
        new PublicChannelUpdateRequest("수정된 채널", "수정된 설명");

    given(channelRepository.findById(channelId))
        .willReturn(Optional.empty());

    // when & then - 수정 실패 확인
    assertThrows(ChannelNotFoundException.class,
        () -> channelService.update(channelId, request));
  }

  @Test
  void 사용자별_채널_조회_성공() {

    // given
    UUID userId = UUID.randomUUID();

    Channel channel =
        new Channel(ChannelType.PUBLIC, "공개 채널", "설명");

    given(readStatusRepository.findAllByUserId(userId))
        .willReturn(List.of());

    given(channelRepository.findAllByTypeOrIdIn(
        eq(ChannelType.PUBLIC), anyList()))
        .willReturn(List.of(channel));

    // when
    List<ChannelDto> result =
        channelService.findAllByUserId(userId);

    // then
    assertEquals(1, result.size());
  }

  @Test
  void 사용자별_채널_조회_빈목록() {

    // given
    UUID userId = UUID.randomUUID();

    given(channelRepository.findAllByTypeOrIdIn(
        eq(ChannelType.PUBLIC), anyList()))
        .willReturn(List.of());

    // when
    List<ChannelDto> result =
        channelService.findAllByUserId(userId);

    // then
    assertTrue(result.isEmpty());
  }

}