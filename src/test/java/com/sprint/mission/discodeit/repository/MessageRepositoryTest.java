package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.ChannelLastMessageAt;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ChannelRepository channelRepository;

  @TestConfiguration
  @EnableJpaAuditing
  static class AuditingTestConfig {

  }

  User user1;
  User user2;
  Channel channel1;
  Channel channel2;
  Message message1;
  Message message2;

  @BeforeEach
  void setUp() {
    user1 = userRepository.save(new User("user1name", "user1@naver.com", "password", null));
    user2 = userRepository.save(new User("user2name", "user2@naver.com", "password", null));
    channel1 = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "channel1Name", "channel1Desc"));
    channel2 = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "channel2Name", "channel2Desc"));
    message1 = messageRepository.save(new Message("content1", channel1, user1, List.of()));
    message2 = messageRepository.save(new Message("content2", channel1, user1, List.of()));
  }

  @Test
  @DisplayName("특정 채널 메세지 페이징")
  void message_find_page_by_channelId_case1() {
    // when
    Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
    Slice<Message> result = messageRepository.findAllByChannelId(channel1.getId(), pageable);
    // then
    assertThat(result.getContent())
        .extracting(Message::getContent)
        .containsExactlyInAnyOrder("content2", "content1");
    assertThat(result.getNumberOfElements()).isEqualTo(2);
    assertThat(result.hasNext()).isFalse();
  }

  @Test
  @DisplayName("특정 채널 메세지 페이징")
  void message_find_page_by_channelId_case2() {
    // given
    Instant cursor = Instant.now().plusSeconds(60);
    // when
    Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
    Slice<Message> result = messageRepository.findAllByChannelIdAndCreatedAtLessThan(
        channel1.getId(), cursor, pageable);
    // then
    assertThat(result.getContent())
        .extracting(Message::getContent)
        .containsExactlyInAnyOrder("content2", "content1");
    assertThat(result.getNumberOfElements()).isEqualTo(2);
    assertThat(result.hasNext()).isFalse();
  }

  @Test
  @DisplayName("특정 채널 내 전체 메세지 호출")
  void message_find_all_channelId() {
    List<Message> messages = messageRepository.findAllByChannelId(channel1.getId());
    assertThat(messages)
        .extracting(Message::getContent)
        .containsExactlyInAnyOrder("content1", "content2");
  }

  @Test
  @DisplayName("특정 채널 마지막 메세지 호출")
  void message_find_top_by_channelId() {
    Optional<Message> message = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
        channel1.getId());
    assertThat(message.get().getContent()).isEqualTo("content2");
  }

  @Test
  @DisplayName("채널별 마지막 메세지 시간 조회")
  void message_find_all_umm_last_time() {
    Instant channel1LastMessageTime = message2.getCreatedAt();
    // when
    List<ChannelLastMessageAt> result = messageRepository.findLastMessageAtByChannelIds(
        List.of(channel1.getId()));
    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).channelId()).isEqualTo(channel1.getId());
    assertThat(result.get(0).lastMessageAt()).isEqualTo(channel1LastMessageTime);
  }
}