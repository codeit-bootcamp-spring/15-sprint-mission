
package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  UserRepository userRepository;

  private Channel channel;
  private User author;

  // 각 테스트 실행 전 기본 데이터 준비
  @BeforeEach
  void setUp() {

    channel = channelRepository.saveAndFlush(
        new Channel(ChannelType.PUBLIC, "테스트 채널", "설명")
    );

    User user =
        new User("testUser", "test@test.com", "123456", null);

    new UserStatus(user, Instant.now());

    author = userRepository.saveAndFlush(user);
  }

  // 테스트용 메시지 저장 메서드
  private Message saveMessage(String content) {
    return messageRepository.saveAndFlush(
        new Message(content, channel, author, List.of())
    );
  }


  // 1. 채널별 메시지 조회 성공
  @Test
  void 채널별_메시지_조회_성공() {

    // given
    saveMessage("안녕하세요");

    // when
    Slice<Message> result =
        messageRepository.findAllByChannelIdWithAuthor(
            channel.getId(),
            Instant.now().plusSeconds(60),
            PageRequest.of(0, 10)
        );

    // then
    assertEquals(1, result.getContent().size());
    assertEquals("안녕하세요",
        result.getContent().get(0).getContent());
  }


  // 2. 채널별 메시지 조회 결과 없음
  @Test
  void 채널별_메시지_조회_빈목록() {

    // given
    // 메시지를 저장하지 않음

    // when
    Slice<Message> result =
        messageRepository.findAllByChannelIdWithAuthor(
            channel.getId(),
            Instant.now(),
            PageRequest.of(0, 10)
        );

    // then
    assertTrue(result.isEmpty());
  }


  // 3. 메시지 페이징 및 정렬
  @Test
  void 메시지_페이징_정렬_성공() {

    // given - 메시지 3개 저장
    saveMessage("Charlie");
    saveMessage("Alice");
    saveMessage("Bob");

    Instant cursor = Instant.now().plusSeconds(60);

    PageRequest pageable = PageRequest.of(
        0, 2, Sort.by("content").ascending()
    );

    // when
    Slice<Message> result =
        messageRepository.findAllByChannelIdWithAuthor(
            channel.getId(), cursor, pageable
        );

    // then
    assertEquals(2, result.getContent().size());
    assertEquals("Alice", result.getContent().get(0).getContent());
    assertEquals("Bob", result.getContent().get(1).getContent());
    assertTrue(result.hasNext());
  }


  // 4. 최근 메시지 작성 시간 조회 성공
  @Test
  void 최근_메시지_시간_조회_성공() {

    // given
    Message message = saveMessage("최근 메시지");

    // when
    Optional<Instant> result =
        messageRepository.findLastMessageAtByChannelId(
            channel.getId()
        );

    // then
    assertTrue(result.isPresent());
    assertEquals(
        message.getCreatedAt().toEpochMilli(),
        result.get().toEpochMilli()
    );
  }


  // 5. 최근 메시지 작성 시간 조회 결과 없음
  @Test
  void 최근_메시지_시간_조회_빈결과() {

    // when
    Optional<Instant> result =
        messageRepository.findLastMessageAtByChannelId(
            channel.getId()
        );

    // then
    assertTrue(result.isEmpty());
  }


  // 6. 채널 메시지 전체 삭제 성공
  @Test
  void 채널_메시지_전체_삭제_성공() {

    // given
    saveMessage("첫 번째 메시지");
    saveMessage("두 번째 메시지");

    // when
    messageRepository.deleteAllByChannelId(channel.getId());
    messageRepository.flush();

    // then
    assertEquals(0, messageRepository.count());
  }


  // 7. 다른 채널의 메시지는 삭제되지 않음
  @Test
  void 다른_채널의_메시지는_유지() {

    // given
    Message target = saveMessage("삭제 대상 메시지");

    Channel otherChannel = channelRepository.saveAndFlush(
        new Channel(ChannelType.PRIVATE, null, null)
    );

    Message otherMessage = messageRepository.saveAndFlush(
        new Message("다른 채널 메시지",
            otherChannel, author, List.of())
    );

    // when
    messageRepository.deleteAllByChannelId(channel.getId());
    messageRepository.flush();

    // then
    assertFalse(messageRepository.existsById(target.getId()));
    assertTrue(messageRepository.existsById(otherMessage.getId()));
  }
}
